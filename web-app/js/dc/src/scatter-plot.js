/**
## Scatter Plot

Includes: [Coordinate Grid Mixin](#coordinate-grid-mixin)

A scatter plot chart

#### dc.scatterPlot(parent[, chartGroup])
Create a scatter plot instance and attach it to the given parent element.

Parameters:

* parent : string|compositeChart - any valid d3 single selector representing typically a dom block element such
as a div, or if this scatter plot is a sub-chart in a [Composite Chart](#composite-chart) then pass in the parent composite chart instance.
* chartGroup : string (optional) - name of the chart group this chart instance should be placed in. Once a chart is placed
in a certain chart group then any interaction with such instance will only trigger events and redraw within the same
chart group.

Return:
A newly created scatter plot instance

```js
// create a scatter plot under #chart-container1 element using the default global chart group
var chart1 = dc.scatterPlot("#chart-container1");
// create a scatter plot under #chart-container2 element using chart group A
var chart2 = dc.scatterPlot("#chart-container2", "chartGroupA");
// create a sub-chart under a composite parent chart
var chart3 = dc.scatterPlot(compositeChart);
```

 **/
dc.scatterPlot = function (parent, chartGroup) {
    var _chart = dc.coordinateGridMixin({});
    var _symbol = d3.svg.symbol();

    var originalKeyAccessor = _chart.keyAccessor();
    _chart.keyAccessor(function (d) { return originalKeyAccessor(d)[0]; });
    _chart.valueAccessor(function (d) { return originalKeyAccessor(d)[1]; });
    _chart.colorAccessor(function (d) { return _chart._groupName; });

    var _locator = function (d) {
        return "translate(" + _chart.x()(_chart.keyAccessor()(d)) + "," +
                              _chart.y()(_chart.valueAccessor()(d)) + ")";
    };

    var _symbolSize = 3;
    var _highlightedSize = 5;

    _symbol.size(function(d) {
        return this.filtered ? Math.pow(_highlightedSize, 2) : Math.pow(_symbolSize, 2);
    });

    dc.override(_chart, "_filter", function(filter) {
        if (!arguments.length) return _chart.__filter();

        return _chart.__filter(dc.filters.RangedTwoDimensionalFilter(filter));
    });

    _chart.plotData = function () {
        var symbols = _chart.chartBodyG().selectAll("path.symbol")
            .data(_chart.data());

        symbols
            .enter()
        .append("path")
            .attr("class", "symbol")
            .attr("opacity", 0)
            .attr("fill", _chart.getColor)
            .attr("transform", _locator);

        dc.transition(symbols, _chart.transitionDuration())
            .attr("opacity", 1)
            .attr("fill", _chart.getColor)
            .attr("transform", _locator)
            .attr("d", _symbol);

        dc.transition(symbols.exit(), _chart.transitionDuration())
            .attr("opacity", 0).remove();
    };

    /**
    #### .symbol([type])
    Get or set the symbol type used for each point. By default a circle. See the D3
    [docs](https://github.com/mbostock/d3/wiki/SVG-Shapes#wiki-symbol_type) for acceptable types;
    Type can be a constant or an accessor.

    **/
    _chart.symbol = function(type) {
        if(!arguments.length) return _symbol.type();
        _symbol.type(type);
        return _chart;
    };

    /**
    #### .symbolSize([radius])
    Set or get radius for symbols, default: 3.

    **/
    _chart.symbolSize = function(s){
        if(!arguments.length) return _symbolSize;
        _symbolSize = s;
        return _chart;
    };

    /**
    #### .highlightedSize([radius])
    Set or get radius for highlighted symbols, default: 4.

    **/
    _chart.highlightedSize = function(s){
        if(!arguments.length) return _highlightedSize;
        _highlightedSize = s;
        return _chart;
    };

    _chart.legendables = function () {
        return [{chart: _chart, name: _chart._groupName, color: _chart.getColor()}];
    };

    _chart.legendHighlight = function (d) {
        resizeSymbolsWhere(function (symbol) {
            return symbol.attr('fill') == d.color;
        }, _highlightedSize);
        _chart.selectAll('.chart-body path.symbol').filter(function () {
            return d3.select(this).attr('fill') != d.color;
        }).classed('fadeout', true);
    };

    _chart.legendReset = function (d) {
        resizeSymbolsWhere(function (symbol) {
            return symbol.attr('fill') == d.color;
        }, _symbolSize);
        _chart.selectAll('.chart-body path.symbol').filter(function () {
            return d3.select(this).attr('fill') != d.color;
        }).classed('fadeout', false);
    };

    function resizeSymbolsWhere(condition, size) {
        var symbols = _chart.selectAll('.chart-body path.symbol').filter(function (d) {
            return condition(d3.select(this));
        });
        var oldSize = _symbol.size();
        _symbol.size(Math.pow(size, 2));
        dc.transition(symbols, _chart.transitionDuration()).attr("d", _symbol);
        _symbol.size(oldSize);
    }

    _chart.setHandlePaths = function () {
        // no handle paths for poly-brushes
    };

    _chart.extendBrush = function () {
        var extent = _chart.brush().extent();
        if (_chart.round()) {
            extent[0] = extent[0].map(_chart.round());
            extent[1] = extent[1].map(_chart.round());

            _chart.g().select(".brush")
                .call(_chart.brush().extent(extent));
        }
        return extent;
    };

    _chart.brushIsEmpty = function (extent) {
        return _chart.brush().empty() || !extent || extent[0][0] >= extent[1][0] || extent[0][1] >= extent[1][1];
    };

    function resizeFiltered(filter) {
        var symbols = _chart.selectAll('.chart-body path.symbol').each(function (d) {
            this.filtered = filter && filter.isFiltered(d.key);
        });

        dc.transition(symbols, _chart.transitionDuration()).attr("d", _symbol);
    }

    _chart._brushing = function () {
        var extent = _chart.extendBrush();

        _chart.redrawBrush(_chart.g());

        if (_chart.brushIsEmpty(extent)) {
            dc.events.trigger(function () {
                _chart.filter(null);
                dc.redrawAll(_chart.chartGroup());
            });

            resizeFiltered(false);

        } else {
            var ranged2DFilter = dc.filters.RangedTwoDimensionalFilter(extent);
            dc.events.trigger(function () {
                _chart.filter(null);
                _chart.filter(ranged2DFilter);
                dc.redrawAll(_chart.chartGroup());
            }, dc.constants.EVENT_DELAY);

            resizeFiltered(ranged2DFilter);
        }
    };

    _chart.setBrushY = function (gBrush) {
        gBrush.call(_chart.brush().y(_chart.y()));
    };

    return _chart.anchor(parent, chartGroup);
};
