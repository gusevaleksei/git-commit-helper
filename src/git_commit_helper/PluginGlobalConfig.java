package git_commit_helper;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import git_commit_helper.PluginGlobalConfig.CommitState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


@State(
        name = "PluginGlobalConfig",
        storages = {@Storage("GitCommitHelperGlobalConfig.xml")}
)

public class PluginGlobalConfig implements PersistentStateComponent<CommitState> {

    private CommitState cmState = new CommitState();

    void setEnableProjectSettings(String projectName) {
        cmState.perProjectSettings.put(projectName, true);
    }

    void setDisableProjectSettings(String projectName) {
        cmState.perProjectSettings.remove(projectName);
    }

    public Boolean getProjectSettings(String projectName) {
        return cmState.perProjectSettings.getOrDefault(projectName, false);
    }

    @Nullable
    @Override
    public CommitState getState() {
        return cmState;
    }

    @Override
    public void loadState(CommitState state) {
        cmState = state;
    }

    @Nullable
    static PluginGlobalConfig getInstance() {
        return ServiceManager.getService(PluginGlobalConfig.class);
    }

    public static class CommitState {
        public String commitMessage = "";
        public String branchRegexp = "";
        private Map<String, Boolean> perProjectSettings = new HashMap<>();
    }
}
