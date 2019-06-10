package git_commit_helper;

import com.intellij.openapi.project.Project;
import git_commit_helper.PluginGlobalConfig.CommitState;


class PluginConfig {
    private PluginGlobalConfig globalConfig;
    private PluginProjectConfig projectConfig;

    private String getProjectName(Project project) {

        return project.getName();
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

//    private PluginGlobalConfig getGlobalConfig() {
//        PluginGlobalConfig globalConfig = PluginGlobalConfig.getInstance();
//        if (globalConfig == null) {
//            globalConfig = new PluginGlobalConfig();
//        }
//        return globalConfig;
//    }
//
//    private PluginProjectConfig getProjectConfig(Project project) {
//        PluginProjectConfig projectConfig = PluginProjectConfig.getInstance(project);
//        if (projectConfig == null) {
//            projectConfig = new PluginProjectConfig();
//        }
//        return projectConfig;
//    }

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
            globalConfig.loadState(cstate);
        }
    }

    boolean isProjectSettingsLevel(Project project) {
//        globalConfig = getGlobalConfig();
        if (project == null) {
            return false;
        }
        return globalConfig.getProjectSettings(getProjectName(project));
    }

    void setSettingsLevel(Project project, boolean isProjectLevel) {
//        globalConfig = getGlobalConfig();
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

//    public String getCommitMessageTemplate(Project project, boolean perProjectSettings) {
//        //    public String commitMessage = "";
//        //    public String branchRegexp = "";
//        //    public Map<String, Boolean> perProjectSettings;
//        CommitState cstate = getCommitState(project, perProjectSettings);
//        return cstate.commitMessage;
//    }
//
//    public void setCommitMessageTemplate(String messageTemplate) {
//
//    }

//    public String getBranchRegexp(Project project, boolean perProjectSettings) {
//        return "";
//    }
//
//    public void setBranchRegexp(String branchRegexp) {
//
//    }
}


//public class PluginConfig implements PersistentStateComponent<CommitState> {
//    private CommitState cmState = new CommitState();
//
//    String getCommitTemplate() {
//        return cmState.commitMessage;
//    }
//
//    String getBranchRegexp() {
//        return cmState.branchRegexp;
//    }
//
//    void setCommitTemplate(String commitMessage) {
//        cmState.commitMessage = commitMessage;
//    }
//
//    void setBranchRegexp(String branchRegexp) {
//        cmState.branchRegexp = branchRegexp;
//    }
//
//    @Nullable
////    @Override
//    public CommitState getState() {
//        return cmState;
//    }
//
////    @Override
//    public void loadState(CommitState state) {
//        cmState = state;
//    }
//
////    @Nullable
//////    static PluginGlobalConfig getInstance(Project project) {
////    static PluginGlobalConfig getInstance() {
////        return ServiceManager.getService(PluginGlobalConfig.class);
////    }
//
//    public static class CommitState {
//        public String commitMessage = "";
//        public String branchRegexp = "";
//    }
//}
