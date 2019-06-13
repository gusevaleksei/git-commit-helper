package git_commit_helper;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.project.Project;
import git_commit_helper.PluginGlobalConfig.CommitState;


class PluginConfig {
    private PluginGlobalConfig globalConfig;
    private PluginProjectConfig projectConfig;

    private String getProjectName(Project project) {
        return project.getName() + project.getLocationHash() + project.getBasePath();
    }

    static PluginConfig getInstance(Project project) {
        PluginConfig cfg = new PluginConfig();

        PluginGlobalConfig globalConfig = PluginGlobalConfig.getInstance();
        if (globalConfig == null) {
            globalConfig = new PluginGlobalConfig();
        }

        PluginProjectConfig projectConfig = PluginProjectConfig.getInstance(project);
        if (projectConfig == null) {
            projectConfig = new PluginProjectConfig();
        }

        cfg.globalConfig = globalConfig;
        cfg.projectConfig = projectConfig;
        return cfg;
    }

    CommitState getCommitState(boolean perProjectSettings) {
        if (perProjectSettings) {
            return projectConfig.getState();
        } else {
            return globalConfig.getState();
        }
    }

    void setCommitState(CommitState cstate, boolean perProjectSettings) {
        if (perProjectSettings) {
            projectConfig.loadState(cstate);
        } else {
            CommitState existing = globalConfig.getState();
            if (existing == null) {
                existing = new CommitState();
            }
            existing.commitMessage = cstate.commitMessage;
            existing.branchRegexp = cstate.branchRegexp;
            globalConfig.loadState(existing);
        }
    }

    boolean isProjectSettingsLevel(Project project) {
        if (project == null) {
            return false;
        }
        return globalConfig.getProjectSettings(getProjectName(project));
    }

    void setSettingsLevel(Project project, boolean isProjectLevel) {
        PluginManager.getLogger().warn("setSettingsLevel " + project.getName() + " " + isProjectLevel);
        if (isProjectLevel) {
            globalConfig.setEnableProjectSettings(getProjectName(project));
        } else {
            globalConfig.setDisableProjectSettings(getProjectName(project));
        }
    }

    String getCommitMessageTemplate(Project project) {
        boolean perProjectSettings = isProjectSettingsLevel(project);
        CommitState cstate = getCommitState(perProjectSettings);
        return cstate.commitMessage;
    }
}
