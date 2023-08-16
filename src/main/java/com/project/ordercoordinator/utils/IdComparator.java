package com.project.ordercoordinator.utils;

import com.project.ordercoordinator.models.Province;

import java.util.Comparator;

public class IdComparator implements Comparator<Province> {
    private final Integer targetId;

    public IdComparator(Integer targetId) {
        this.targetId = targetId;
    }

    @Override
    public int compare(Province o1, Province o2) {
        Integer dist1 = Math.abs(o1.getId() - targetId);
        Integer dist2 = Math.abs(o2.getId() - targetId);
        return Integer.compare(dist1, dist2);
    }
}
