package com.shrey.kc.kcui.objects;

import java.util.Map;

public enum ViewConfigHolder {
    INSTANCE;

    private Map<Integer, Integer> menuSelectionToLayoutIdMap;
    private Map<Integer, String> menuSelectionToActionMap;

    public Integer getLayoutForMenu(int menuId) {
        return menuSelectionToLayoutIdMap.getOrDefault(menuId, -1);
    }

    public void setLayoutForMenu(int menuId, int layoutId) {
        menuSelectionToLayoutIdMap.put(menuId, layoutId);
    }

    public String getActionForMenu(int menuId) {
        return menuSelectionToActionMap.getOrDefault(menuId, "oops");
    }

    public void setActionForMenu(int menuId, String action) {
        menuSelectionToActionMap.put(menuId, action);
    }

}
