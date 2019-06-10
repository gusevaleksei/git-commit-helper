package git_commit_helper;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class GitCommitHelperSettings implements SearchableConfigurable {

    private GitCommitHelperSettingsGUI gui;

    private final Project project;

//    public GitCommitHelperSettings() {
//        this.project = null;
//    }

    public GitCommitHelperSettings(@NotNull Project project) {
        this.project = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Git Commit Helper Plugin";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @NotNull
    @Override
    public String getId() {
        return "preference.GitCommitHelperSettings";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        gui = new GitCommitHelperSettingsGUI();
        gui.createUI(project);
//        PluginManager.getLogger().error("GitCommitHelperSettings JComponent createComponent");
        return gui.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return gui.isModified(project);
    }

    @Override
    public void apply() throws ConfigurationException {
        gui.apply(project);
    }

    @Override
    public void reset() {
        gui.reset(project);
    }

    @Override
    public void disposeUIResources() {
        gui = null;
    }
}
