/*
 * Copyright 2014 Massachusetts General Hospital
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.annotopia.grails.controllers

import org.annotopia.groovy.service.store.BaseController
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.riot.RDFLanguages
import org.commonsemantics.grails.users.model.User

import com.github.jsonldjava.core.JsonLdOptions
import com.github.jsonldjava.core.JsonLdProcessor
import com.github.jsonldjava.utils.JsonUtils
import com.hp.hpl.jena.query.Dataset
import com.hp.hpl.jena.rdf.model.Model

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class ProtectedController extends BaseController {
	
	private final RESPONSE_CONTENT_TYPE = "application/json;charset=UTF-8";
	
	// outCmd (output command) constants
	private final OUTCMD_NONE = "none";
	private final OUTCMD_FRAME = "frame";
	private final OUTCMD_CONTEXT = "context";
	
	// incGph (Include graph) constants
	private final INCGPH_YES = "true";
	private final INCGPH_NO = "false";
	
	def springSecurityService
	def openAnnotationVirtuosoService
	def openAnnotationStorageService
	
	def openAnnotationWithPermissionsVirtuosoService
	def openAnnotationWithPermissionsStorageService
	
	/*
	 * Loading by primary key is usually more efficient because it takes
	 * advantage of Hibernate's first-level and second-level caches
	 */
	protected def injectUserProfile() {
		def principal = springSecurityService.principal
		if(principal.equals("anonymousUser")) {
			redirect(controller: "login", action: "index");
		} else {
			String userId = principal.id
			def user = User.findById(userId);
			if(user==null) {
				log.error "Error:User not found for id: " + userId
				render (view:'error', model:[message: "User not found for id: "+userId]);
			}
			user
		}
	}
	
	def index = {
		render(view: "index", model: [menu: 'index'])
	}
	
	/**
	 * Returns the view that allows to browse and filter existing annotations.
	 * The annotations are loaded through Ajax.
	 */
	def annotations = {
		def loggedUser = injectUserProfile();
		render(view: "annotations", model: [menu: 'myannotations', appBaseUrl: request.getContextPath(), loggedUser: loggedUser])
	}
	
	/**
	 * Returns all the annotations that meet the filtering criteria.
	 * This method should be protected and therefore assumes we 
	 * know the credentials of the user requesting the annotation.
	 */
	def getAnnotation = {
		long startTime = System.currentTimeMillis();
		
		// Authentication data
		def loggedUser = injectUserProfile();
		def agentKey = "user:" + loggedUser.id;
		
		// Response format parametrization and constraints
		def outCmd = (request.JSON.outCmd!=null)?request.JSON.outCmd:OUTCMD_NONE;
		if(params.outCmd!=null) outCmd = params.outCmd;
		def incGph = (request.JSON.incGph!=null)?request.JSON.incGph:INCGPH_NO;
		if(params.incGph!=null) incGph = params.incGph;
		if(outCmd==OUTCMD_FRAME && incGph==INCGPH_YES) {
			log.warn("[" + agentKey + "] Invalid options, framing does not currently support Named Graphs");
			def message = 'Invalid options, framing does not currently support Named Graphs';
			render(status: 401, text: returnMessage(agentKey, "rejected", message, startTime),
				contentType: "text/json", encoding: "UTF-8");
			return;
		}
		
		// Pagination
		def max = (request.JSON.max!=null)?request.JSON.max:"10";
		if(params.max!=null) max = params.max;
		def offset = (request.JSON.offset!=null)?request.JSON.offset:"0";
		if(params.offset!=null) offset = params.offset;
		
		// Target filters
		def tgtUrl = request.JSON.tgtUrl
		if(params.tgtUrl!=null) tgtUrl = params.tgtUrl;
		def tgtFgt = (request.JSON.tgtFgt!=null)?request.JSON.tgtFgt:"true";
		if(params.tgtFgt!=null) tgtFgt = params.tgtFgt;
		
		// Facets
		def sources = request.JSON.sources
		if(params.sources!=null) sources = params.sources;
		def sourcesFacet = []
		if(sources) sourcesFacet = sources.split(",");
		def permissions = request.JSON.permissions
		if(params.permissions!=null) permissions = params.permissions;
		def permissionsFacet = []
		if(permissions) permissionsFacet = permissions.split(",");
		def motivations = request.JSON.motivations
		if(params.motivations!=null) motivations = params.motivations;
		def motivationsFacet = []
		if(motivations) motivationsFacet = motivations.split(",");
		
		try {
			int annotationsTotal = openAnnotationWithPermissionsVirtuosoService
				.countAnnotationGraphs(agentKey, loggedUser, tgtUrl, tgtFgt, permissionsFacet, motivationsFacet);
			
			// Check for results offset validity
			int annotationsPages = (annotationsTotal/Integer.parseInt(max));
			if(annotationsTotal>0 && Integer.parseInt(offset)>0 && Integer.parseInt(offset)>=annotationsTotal) {
				def message = 'The requested page ' + offset +
					' does not exist, the page index limit is ' + (annotationsPages==0?"0":(annotationsPages-1));
				render(status: 401, text: returnMessage("user:"+loggedUser.id, "rejected", message, startTime),
					contentType: "text/json", encoding: "UTF-8");
				return;
			}
			
			// Target URLs management
			List<String> tgtUrls;
			if(tgtUrl!=null) {
				tgtUrls = new ArrayList<String>();
				tgtUrls.add(tgtUrl);
			}
			
			// TODO Add bibliogrpahic identity management   
			Set<Dataset> annotationGraphs = openAnnotationWithPermissionsStorageService
				.listAnnotation(agentKey, loggedUser, max, offset, tgtUrls, tgtFgt, null, null, incGph, permissionsFacet, motivationsFacet);
			
			def summaryPrefix = '"total":"' + annotationsTotal + '", ' +
					'"pages":"' + annotationsPages + '", ' +
					'"duration": "' + (System.currentTimeMillis()-startTime) + 'ms", ' +
					'"offset": "' + offset + '", ' +
					'"max": "' + max + '", ' +
					'"items":[';

			Object contextJson = null;
			response.contentType = RESPONSE_CONTENT_TYPE	
			if(annotationGraphs!=null) {
				response.outputStream << '{"status":"results", "result": {' + summaryPrefix	
				boolean firstStreamed = false // To add the commas between items
				annotationGraphs.each { annotationGraph ->
					if(firstStreamed) response.outputStream << ','
					if(outCmd==OUTCMD_NONE) {
						if(incGph==INCGPH_NO) {
							if(annotationGraph.listNames().hasNext()) {
								Model m = annotationGraph.getNamedModel(annotationGraph.listNames().next());
								RDFDataMgr.write(response.outputStream, m.getGraph(), RDFLanguages.JSONLD);
							}
						} else {
							RDFDataMgr.write(response.outputStream, annotationGraph, RDFLanguages.JSONLD);
						}
					} else {
						// This serializes with and according to the context
						if(contextJson==null) {
							if(outCmd==OUTCMD_CONTEXT) {
								contextJson = JsonUtils.fromInputStream(callExternalUrl(agentKey, grailsApplication.config.annotopia.jsonld.openannotation.context));
							} else if(outCmd==OUTCMD_FRAME) {
								contextJson = JsonUtils.fromInputStream(callExternalUrl(agentKey, grailsApplication.config.annotopia.jsonld.openannotation.framing));						
							}
						}

						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						if(incGph==INCGPH_NO) {
							if(annotationGraph.listNames().hasNext()) {
								Model m = annotationGraph.getNamedModel(annotationGraph.listNames().next());
								RDFDataMgr.write(baos, m.getGraph(), RDFLanguages.JSONLD);
							}
						} else {
							RDFDataMgr.write(baos, annotationGraph, RDFLanguages.JSONLD);
						}
						
						if(outCmd==OUTCMD_CONTEXT) {
							Object compact = JsonLdProcessor.compact(JsonUtils.fromString(baos.toString()), contextJson,  new JsonLdOptions());
							response.outputStream << JsonUtils.toPrettyString(compact)
						}  else if(outCmd==OUTCMD_FRAME) {
							Object framed =  JsonLdProcessor.frame(JsonUtils.fromString(baos.toString().replace('"@id" : "urn:x-arq:DefaultGraphNode",','')), contextJson, new JsonLdOptions());
							response.outputStream << JsonUtils.toPrettyString(framed)
						}
					}
					firstStreamed = true;
				}
			} else {
				// No Annotation Sets found with the specified criteria
				log.info("[" + agentKey + "] No Annotation found with the specified criteria");			
				response.outputStream << '{"status":"nocontent","message":"No results with the chosen criteria" , "result": {' + summaryPrefix
			}
			response.outputStream <<  ']}}';
			response.outputStream.flush()
		} catch(Exception e) {
			trackException(agentKey, "", "FAILURE: Retrieval of the list of existing annotations failed " + e.getMessage());
		}	
	}
	
	/**
	 * Returns the view that allows to search and filter existing annotations.
	 * The annotations are loaded through Ajax.
	 */
	def search = {
		def loggedUser = injectUserProfile();
		render(view: "search", model: [menu: 'search', appBaseUrl: request.getContextPath(), loggedUser: loggedUser])
	}
	
	/**
	 * Returns all the annotations that meet the search criteria.
	 * This method should be protected and therefore assumes we
	 * know the credentials of the user requesting the annotation.
	 */
	def searchAnnotation = {
		long startTime = System.currentTimeMillis();
		
		def loggedUser = injectUserProfile();
		def agentKey = "user:" + loggedUser.id;
		
		// Response format parametrization and constraints
		def outCmd = (request.JSON.outCmd!=null)?request.JSON.outCmd:OUTCMD_NONE;
		if(params.outCmd!=null) outCmd = params.outCmd;
		def incGph = (request.JSON.incGph!=null)?request.JSON.incGph:INCGPH_NO;
		if(params.incGph!=null) incGph = params.incGph;
		if(outCmd==OUTCMD_FRAME && incGph==INCGPH_YES) {
			log.warn("[" + "user:"+loggedUser.id + "] Invalid options, framing does not currently support Named Graphs");
			def message = 'Invalid options, framing does not currently support Named Graphs';
			render(status: 401, text: returnMessage(agentKey, "rejected", message, startTime),
				contentType: "text/json", encoding: "UTF-8");
			return;
		}
		
		// Pagination
		def max = (request.JSON.max!=null)?request.JSON.max:"10";
		if(params.max!=null) max = params.max;
		def offset = (request.JSON.offset!=null)?request.JSON.offset:"0";
		if(params.offset!=null) offset = params.offset;
		
		// Target filters
		def tgtUrl = request.JSON.tgtUrl
		if(params.tgtUrl!=null) tgtUrl = params.tgtUrl;
		def tgtFgt = (request.JSON.tgtFgt!=null)?request.JSON.tgtFgt:"true";
		if(params.tgtFgt!=null) tgtFgt = params.tgtFgt;
		
		// Facets
		def text = request.JSON.text
		if(params.text!=null) text = params.text;
		def sources = request.JSON.sources
		if(params.sources!=null) sources = params.sources;
		def sourcesFacet = []
		if(sources) sourcesFacet = sources.split(",");
		def permissions = request.JSON.permissions
		if(params.permissions!=null) permissions = params.permissions;
		def permissionsFacet = []
		if(permissions) permissionsFacet = permissions.split(",");
		def motivations = request.JSON.motivations
		if(params.motivations!=null) motivations = params.motivations;
		def motivationsFacet = []
		if(motivations) motivationsFacet = motivations.split(",");

		// Inclusions		
		def inclusions = request.JSON.inclusions
		if(params.inclusions!=null) inclusions = params.inclusions;
		def inclusionsFacet = []
		if(inclusions) inclusionsFacet = inclusions.split(",");
		
		// Currently unusued, planned
		def tgtExt = request.JSON.tgtExt
		def tgtIds = request.JSON.tgtIds
		def flavor = request.JSON.flavor

		try {
			long countingStart = System.currentTimeMillis();
			int annotationsTotal = openAnnotationWithPermissionsVirtuosoService
				.countAnnotationGraphs(agentKey, loggedUser, tgtUrl, tgtFgt, text, permissionsFacet, sourcesFacet, motivationsFacet, inclusionsFacet);
				
			// Check for results offset validity
			int annotationsPages = (annotationsTotal/Integer.parseInt(max));
			if(annotationsTotal>0 && Integer.parseInt(offset)>0 && Integer.parseInt(offset)>=annotationsTotal) {
				def message = 'The requested page ' + offset +
					' does not exist, the page index limit is ' + (annotationsPages==0?"0":(annotationsPages-1));
				render(status: 401, text: returnMessage(agentKey, "rejected", message, startTime),
					contentType: "text/json", encoding: "UTF-8");
				return;
			}
			
			// Target URLs management
			List<String> tgtUrls;
			if(tgtUrl!=null) {
				tgtUrls = new ArrayList<String>();
				tgtUrls.add(tgtUrl);
			}
			log.trace "[" + agentKey + "] TIME DURATION (call.countAnnotationGraphs): " + (System.currentTimeMillis()-countingStart);
			
			long retrievalStart = System.currentTimeMillis();
			Set<Dataset> annotationGraphs = openAnnotationWithPermissionsStorageService
				.listAnnotation(agentKey, loggedUser, max, offset, tgtUrls, tgtFgt, tgtExt, tgtIds, incGph, text, permissionsFacet, sourcesFacet, motivationsFacet, inclusionsFacet);
			
			def summaryPrefix = '"total":"' + annotationsTotal + '", ' +
					'"pages":"' + annotationsPages + '", ' +
					'"duration": "' + (System.currentTimeMillis()-startTime) + 'ms", ' +
					'"offset": "' + offset + '", ' +
					'"max": "' + max + '", ' +
					'"items":[';
			log.trace "[" + agentKey + "] TIME DURATION (call.retrieveAnnotationGraphsNames): " + (System.currentTimeMillis()-retrievalStart);

			Object contextJson = null;
			response.contentType = RESPONSE_CONTENT_TYPE
			if(annotationGraphs!=null) {
				response.outputStream << '{"status":"results", "result": {' + summaryPrefix
				boolean firstStreamed = false // To add the commas between items
				annotationGraphs.each { annotationGraph ->
					if(firstStreamed) response.outputStream << ','
					if(outCmd==OUTCMD_NONE) {
						if(incGph==INCGPH_NO) {
							if(annotationGraph.listNames().hasNext()) {
								Model m = annotationGraph.getNamedModel(annotationGraph.listNames().next());
								RDFDataMgr.write(response.outputStream, m.getGraph(), RDFLanguages.JSONLD);
							}
						} else {
							RDFDataMgr.write(response.outputStream, annotationGraph, RDFLanguages.JSONLD);
						}
					} else {
						// This serializes with and according to the context
						if(contextJson==null) {
							if(outCmd==OUTCMD_CONTEXT) {
								contextJson = JsonUtils.fromInputStream(callExternalUrl(agentKey, grailsApplication.config.annotopia.jsonld.openannotation.context));
							} else if(outCmd==OUTCMD_FRAME) {
								contextJson = JsonUtils.fromInputStream(callExternalUrl(agentKey, grailsApplication.config.annotopia.jsonld.openannotation.framing));
							}
						}

						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						if(incGph==INCGPH_NO) {
							if(annotationGraph.listNames().hasNext()) {
								Model m = annotationGraph.getNamedModel(annotationGraph.listNames().next());
								RDFDataMgr.write(baos, m.getGraph(), RDFLanguages.JSONLD);
							}
						} else {
							RDFDataMgr.write(baos, annotationGraph, RDFLanguages.JSONLD);
						}
						
						if(outCmd==OUTCMD_CONTEXT) {
							Object compact = JsonLdProcessor.compact(JsonUtils.fromString(baos.toString()), contextJson,  new JsonLdOptions());
							response.outputStream << JsonUtils.toPrettyString(compact)
						}  else if(outCmd==OUTCMD_FRAME) {
							Object framed =  JsonLdProcessor.frame(JsonUtils.fromString(baos.toString().replace('"@id" : "urn:x-arq:DefaultGraphNode",','')), contextJson, new JsonLdOptions());
							response.outputStream << JsonUtils.toPrettyString(framed)
						}
					}
					firstStreamed = true;
				}
			} else {
				// No Annotation Sets found with the specified criteria
				log.info("[" + agentKey + "] No Annotation found with the specified criteria");
				response.outputStream << '{"status":"nocontent","message":"No results with the chosen criteria" , "result": {' + summaryPrefix
			}
			response.outputStream <<  ']}}';
			response.outputStream.flush()
		} catch(Exception e) {
			trackException(agentKey, "", "FAILURE: Retrieval of the list of existing annotations failed " + e.getMessage());
		}
	}
	
	def profile = {
		def user = injectUserProfile()
		render(view: "profile", model: [menu: 'index', user: user])
	}
	
	// --------------------------------------------
	//  Exceptions and Logging utils
	// --------------------------------------------
	private void trackException(def userId, String textContent, String msg) {
		logException(userId, msg);
		response.status = 500
		return;
	}
	
	private def logInfo(def userId, message) {
		log.info("[" + userId + "] " + message);
	}
	
	private def logDebug(def userId, message) {
		log.debug("[" + userId + "] " + message);
	}
	
	private def logWarning(def userId, message) {
		log.warn("[" + userId + "] " + message);
	}
	
	private def logException(def userId, message) {
		log.error("[" + userId + "] " + message);
	}
}
