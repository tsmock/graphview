// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.graphview.core.property;

import java.util.Objects;

import org.openstreetmap.josm.plugins.graphview.core.access.AccessParameters;
import org.openstreetmap.josm.plugins.graphview.core.data.DataSource;
import org.openstreetmap.josm.plugins.graphview.core.data.TagGroup;
import org.openstreetmap.josm.plugins.graphview.core.util.ValueStringParser;

public class RoadIncline implements RoadPropertyType<Float> {

    @Override
    public <N, W, R, M> Float evaluateN(N node, AccessParameters accessParameters,
            DataSource<N, W, R, M> dataSource) {
        return null;
    }

    @Override
    public <N, W, R, M> Float evaluateW(W way, boolean forward, AccessParameters accessParameters,
            DataSource<N, W, R, M> dataSource) {
        Objects.requireNonNull(way, "way");
        Objects.requireNonNull(dataSource, "dataSource");

        TagGroup tags = dataSource.getTagsW(way);
        String inclineString = tags.getValue("incline");

        if (inclineString != null) {
            Float incline = ValueStringParser.parseIncline(inclineString);
            if (incline != null) {
                if (!forward) {
                    incline = -incline;
                }
                return incline;
            }
        }

        return null;
    }

    @Override
    public boolean isUsable(Object propertyValue, AccessParameters accessParameters) {
        assert propertyValue instanceof Float;

        float incline = (Float) propertyValue;

        Float maxInclineUp =
            accessParameters.getVehiclePropertyValue(VehiclePropertyTypes.MAX_INCLINE_UP);
        Float maxInclineDown =
            accessParameters.getVehiclePropertyValue(VehiclePropertyTypes.MAX_INCLINE_DOWN);

        if (maxInclineUp != null && incline > maxInclineUp) {
            return false;
        } else if (maxInclineDown != null && -incline > maxInclineDown) {
            return false;
        } else {
            return true;
        }
    }

}
