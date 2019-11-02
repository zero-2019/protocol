package com.chuangxin.monitor.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GatherSpotEntity  implements RowMapper<GatherSpotEntity> {

    private String datcentId;

    private String gatherSpot;

    public String getDatcentId() {
        return datcentId;
    }

    public void setDatcentId(String datcentId) {
        this.datcentId = datcentId;
    }

    public String getGatherSpot() {
        return gatherSpot;
    }

    public void setGatherSpot(String gatherSpot) {
        this.gatherSpot = gatherSpot;
    }

    public GatherSpotEntity() {
    }
    public GatherSpotEntity(String datcentId, String gatherSpot) {
        this.datcentId = datcentId;
        this.gatherSpot = gatherSpot;
    }

    @Override
    public String toString() {
        return "GatherSpotEntity{" +
                "datcentId='" + datcentId + '\'' +
                ", gatherSpot='" + gatherSpot + '\'' +
                '}';
    }

    @Override
    public GatherSpotEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        GatherSpotEntity gatherSpotEntity = new GatherSpotEntity();
        gatherSpotEntity.setDatcentId(rs.getString("datcentId"));
        gatherSpotEntity.setGatherSpot(rs.getString("gatherSpot"));
        return  gatherSpotEntity;

    }


}
