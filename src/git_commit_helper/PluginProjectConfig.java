package git_commit_helper;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import git_commit_helper.PluginGlobalConfig.CommitState;
import org.jetbrains.annotations.Nullable;


@State(
        name = "PluginProjectConfig",
        storages = {@Storage("GitCommitHelperConfig.xml")}
)

public class PluginProjectConfig implements PersistentStateComponent<CommitState> {

    private CommitState cmState = new CommitState();

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
    static PluginProjectConfig getInstance(Project project) {
        return ServiceManager.getService(project, PluginProjectConfig.class);
    }
}
