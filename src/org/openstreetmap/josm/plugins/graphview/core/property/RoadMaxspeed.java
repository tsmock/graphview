// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.graphview.core.property;

import java.util.Objects;

import org.openstreetmap.josm.plugins.graphview.core.access.AccessParameters;
import org.openstreetmap.josm.plugins.graphview.core.data.DataSource;
import org.openstreetmap.josm.plugins.graphview.core.data.TagGroup;
import org.openstreetmap.josm.plugins.graphview.core.util.ValueStringParser;

public class RoadMaxspeed implements RoadPropertyType<Float> {

    private DataSource<?, ?, ?, ?> lastDataSource;

    /**
     * (re)creates information like boundaries if data source has changed
     * since last call to {@link #evaluate(Object, boolean, AccessParameters, DataSource)}
     */
    private <N, W, R, M> void initializeIfNecessary(DataSource<N, W, R, M> dataSource) {

        if (dataSource != lastDataSource) {

            /*
             *
             * currently no activities;
             * place boundaries or similar features can be handled here
             * once there is consensus on the topic of implicit maxspeeds, trafficzones etc.
             *
             */

            lastDataSource = dataSource;
        }
    }

    @Override
    public <N, W, R, M> Float evaluateN(N node, AccessParameters accessParameters,
            DataSource<N, W, R, M> dataSource) {
        Objects.requireNonNull(node);
        Objects.requireNonNull(dataSource);

        initializeIfNecessary(dataSource);

        return evaluateTags(dataSource.getTagsN(node));
    }

    @Override
    public <N, W, R, M> Float evaluateW(W way, boolean forward, AccessParameters accessParameters,
            DataSource<N, W, R, M> dataSource) {
        Objects.requireNonNull(dataSource);
        Objects.requireNonNull(way);

        initializeIfNecessary(dataSource);

        return evaluateTags(dataSource.getTagsW(way));
    }

    private Float evaluateTags(TagGroup tags) {
        String maxspeedString = tags.getValue("maxspeed");

        if (maxspeedString != null) {
            return ValueStringParser.parseSpeed(maxspeedString);
        }

        return null;
    }

    @Override
    public boolean isUsable(Object propertyValue, AccessParameters accessParameters) {
        return propertyValue instanceof Float;
    }

}
