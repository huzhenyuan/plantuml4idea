package org.plantuml.idea.toolwindow;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.plantuml.idea.rendering.RenderCacheItem;

import java.util.LinkedHashMap;
import java.util.Map;

@State(name = "PlantUmlSelectedPages", storages = {@Storage(file = StoragePathMacros.APP_CONFIG + "/plantuml-selectedPages.xml")})
public class SelectedPagePersistentStateComponent implements PersistentStateComponent<SelectedPagePersistentStateComponent.SelectedPages> {

    public static final int MAX_ENTRIES = 50;

    private SelectedPages state = new SelectedPages();

    public SelectedPagePersistentStateComponent() {
    }

    @Override
    @NotNull
    public SelectedPages getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull SelectedPages state) {
        this.state = state;
    }

    public int getPage(String sourceFilePath) {
        Integer integer = state.map.get(sourceFilePath);
        if (integer == null) {
            integer = -1;
        }
        return integer;
    }

    public void setPage(int selectedPage, RenderCacheItem displayedItem) {
        if (displayedItem != null) {
            if (selectedPage == -1) {
                state.map.remove(displayedItem.getSourceFilePath());
            } else {
                state.map.put(displayedItem.getSourceFilePath(), selectedPage);
            }
        }
    }

    public static class SelectedPages {
        private Map<String, Integer> map = new MyLinkedHashMap<String, Integer>(MAX_ENTRIES);

        public Map<String, Integer> getMap() {
            return map;
        }

        public void setMap(Map<String, Integer> map) {
            this.map = map;
        }

    }

    private static class MyLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        private final int maxEntries;

        public MyLinkedHashMap(int maxEntries) {
            super(maxEntries + 1, 1, true);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxEntries;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("maxEntries", maxEntries)
                    .toString();
        }
    }
}
