package git_commit_helper;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.project.Project;
import git_commit_helper.PluginGlobalConfig.CommitState;

import javax.swing.*;


public class GitCommitHelperSettingsGUI {
    private JPanel rootPanel;
    private JPanel templateOpts;
    private JPanel branchOpts;
    private JLabel branchLabel;
    private JLabel branchLabel2;
    private JLabel branchLabel3;
    private JTextArea commitMessageTextBox;
    private JTextField regexpPatternTextField;
    private JRadioButton globalSettingsRadio;
    private JRadioButton projectSettingsRadio;
    private PluginGlobalConfig globalConfig;
    private PluginProjectConfig projectConfig;

    private PluginConfig config;
    private boolean currentProjectSettings;

    private CommitState getUICState() {
        CommitState uiCState = new CommitState();
        uiCState.commitMessage = commitMessageTextBox.getText();
        uiCState.branchRegexp = regexpPatternTextField.getText();
        return uiCState;
    }

    private void setProjectLevel() {
        globalSettingsRadio.setSelected(false);
        projectSettingsRadio.setSelected(true);
        currentProjectSettings = true;
    }

    private void setGlobalLevel() {
        globalSettingsRadio.setSelected(true);
        projectSettingsRadio.setSelected(false);
        currentProjectSettings = false;
    }

    void loadFields(boolean perProjectSettings) {
        CommitState CState = config.getCommitState(perProjectSettings);
        commitMessageTextBox.setText(CState.commitMessage);
        regexpPatternTextField.setText(CState.branchRegexp);
        currentProjectSettings = perProjectSettings;
    }

    void createUI(Project project) {

        boolean perProjectSettings;
        config = PluginConfig.getInstance(project);

        if (!project.isOpen()) {
            setGlobalLevel();
            perProjectSettings = false;
        } else {
            perProjectSettings = config.isProjectSettingsLevel(project);
        }

        if (perProjectSettings) {
            setProjectLevel();
        } else {
            setGlobalLevel();
        }

        loadFields(perProjectSettings);
    }

    JPanel getRootPanel() {
        return rootPanel;
    }

    boolean isModified(Project project) {
//        config = new PluginConfig();

        boolean isModified = false;

        boolean isConfProjLevel = config.isProjectSettingsLevel(project);
        boolean isCurrProjLevel = currentProjectSettings;
        boolean isUIProjLevel = projectSettingsRadio.isSelected();
//        PluginManager.getLogger().warn("isModified "+isConfProjLevel+" "+isCurrProjLevel+" "+isUIProjLevel);

        if (isCurrProjLevel != isUIProjLevel) {
            loadFields(isUIProjLevel);
        }

        if (isUIProjLevel != isConfProjLevel) {
            isModified = true;
        }

        boolean perProjectSettings = projectSettingsRadio.isSelected();
        CommitState CState = config.getCommitState(perProjectSettings);
        CommitState uiCState = getUICState();
        boolean templateChanged = !uiCState.commitMessage.equals(CState.commitMessage);
        boolean regexpChanged = !uiCState.branchRegexp.equals(CState.branchRegexp);

        boolean fieldsChanged = isModified || templateChanged || regexpChanged;

        //PluginManager.getLogger().error("GitCommitHelperSettings JComponent createComponent");
        PluginManager.getLogger().warn("isModified finally " + fieldsChanged);

        return fieldsChanged;
    }

    void apply(Project project) {
        boolean perProjectSettings = projectSettingsRadio.isSelected();
        PluginManager.getLogger().warn("apply per project " + perProjectSettings + " current:" + currentProjectSettings);
        config.setSettingsLevel(project, perProjectSettings);
        config.setCommitState(getUICState(), perProjectSettings);
        currentProjectSettings = perProjectSettings;
    }

    void reset(Project project) {
        createUI(project);
    }
}
