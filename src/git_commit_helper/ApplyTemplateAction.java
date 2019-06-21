package git_commit_helper;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.impl.ProjectLevelVcsManagerImpl;
import com.intellij.openapi.vcs.ui.Refreshable;
import git4idea.GitLocalBranch;
import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static git_commit_helper.Consts.*;


public class ApplyTemplateAction extends AnAction implements DumbAware {

    @NotNull
    private static String getPresetCommitMessage(@Nullable AnActionEvent e) {
        if (e == null) {
            return "";
        } else {
            Refreshable panel = Refreshable.PANEL_KEY.getData(e.getDataContext());
            if (panel instanceof CheckinProjectPanel) {
                CheckinProjectPanel chPan = (CheckinProjectPanel) panel;
                return chPan.getCommitMessage();
            } else {
                return "";
            }
        }
    }

    private static String extractBranchName(Project project) {
        String branch = "";
        ProjectLevelVcsManager instance = ProjectLevelVcsManagerImpl.getInstance(project);
        if (instance.checkVcsIsActive("Git")) {

            GitRepository repo = GitBranchUtil.getCurrentRepository(project);
            if (repo != null) {
                GitLocalBranch currentBranch = repo.getCurrentBranch();
                if (currentBranch != null) {
                    branch = currentBranch.getName().trim();
                }
            }
        } else {
            PluginManager.getLogger().error("Not GIT, Other VCS not supported");
        }
        return branch;
    }

    private String applyTemplate(String commitMessage, String branchName, String commitTemplate, String branchRegexp) {

        if (commitTemplate.isEmpty()) {
            return commitMessage;
        }

        String finalCommitMessage;
        if (commitTemplate.toLowerCase().contains(PRESENT_COMMIT_IS_CONTAINS)) {
            finalCommitMessage = commitTemplate.replaceAll(PRESENT_COMMIT_PLACEHOLDER, commitMessage);
        } else {
            finalCommitMessage = commitTemplate + " " + commitMessage;
        }

        finalCommitMessage = finalCommitMessage.replaceAll(BRANCH_PLACEHOLDER, branchName);

        // regexp need here
        // if (!branchRegexp.equalsIgnoreCase("")) {
        // }

        return finalCommitMessage;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final CommitMessageI checkinPanel = getCheckinPanel(e);
        if (checkinPanel == null) {
            return;
        }

        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        String branchName = extractBranchName(project);
        String presentCommitMessage = getPresetCommitMessage(e);

        PluginConfig config = PluginConfig.getInstance(project);

        String finalCommitMessage = applyTemplate(
                presentCommitMessage,
                branchName,
                config.getCommitMessageTemplate(project),
                config.getBranchRegexp(project));

        checkinPanel.setCommitMessage(finalCommitMessage);
    }


    @Nullable
    private static CommitMessageI getCheckinPanel(@Nullable AnActionEvent e) {
        if (e == null) {
            return null;
        }
        Refreshable data = Refreshable.PANEL_KEY.getData(e.getDataContext());
        if (data instanceof CommitMessageI) {
            return (CommitMessageI) data;
        }
        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
    }
}
