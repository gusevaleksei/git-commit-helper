package git_commit_helper;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.containers.hash.HashMap;
import git_commit_helper.PluginGlobalConfig.CommitState;
import org.jetbrains.annotations.Nullable;


@State(
        name = "PluginGlobalConfig",
        storages = {@Storage("GitCommitHelperGlobalConfig.xml")}
)

public class PluginGlobalConfig implements PersistentStateComponent<CommitState> {

    private CommitState cmState = new CommitState();
    //Messages.showErrorDialog("getProjectSettings " + cmState.perProjectSettings, "");
    //Messages.showInfoMessage();

    void setEnableProjectSettings(String projectName) {
        cmState.perProjectSettings.put(projectName, true);
        PluginManager.getLogger().warn("setEnableProjectSettings done " + projectName + " res:" + cmState);
    }

    void setDisableProjectSettings(String projectName) {
        cmState.perProjectSettings.remove(projectName);
    }

    Boolean getProjectSettings(String projectName) {
        return cmState.perProjectSettings.getOrDefault(projectName, false);
    }

    @Nullable
    @Override
    public CommitState getState() {
        // save to storage
        return cmState;
    }

    @Override
    public void loadState(CommitState state) {
        // load from storage
        cmState = state;
    }

    @Nullable
    static PluginGlobalConfig getInstance() {
        return ServiceManager.getService(PluginGlobalConfig.class);
    }

    public static class CommitState {
        public String commitMessage = "";
        public String branchRegexp = "";
        public HashMap<String, Boolean> perProjectSettings = new HashMap<>();
    }
}
