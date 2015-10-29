package in.ashwanthkumar.gocd.github.util;

import in.ashwanthkumar.utils.lang.StringUtils;

import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.go.plugin.api.logging.Logger;

public class BranchFilter {

	private static Logger LOGGER = Logger.getLoggerFor(BranchFilter.class);
    public static final String SEPARATOR = ",(?=[^\\}]*(?:\\{|$))"; // "," not between {} (because of glob format)
    private static final String GLOB_PATTERN = "glob:";

    private final Set<PathMatcher> selectedBranches = new HashSet<>();
    private final Set<PathMatcher> blacklistedBranches = new HashSet<>();

    public BranchFilter(String selectedBranchesOption, String blacklistedBranchesOption) {
    	if (StringUtils.isNotEmpty(selectedBranchesOption)) {
            for (String branch : selectedBranchesOption.split(SEPARATOR)) {
            	PathMatcher matcher = matcherFromString(branch);
            	if (matcher!=null)
            		selectedBranches.add(matcher);
            }
        }
    	
        if (StringUtils.isNotEmpty(blacklistedBranchesOption)) {
            for (String branch : blacklistedBranchesOption.split(SEPARATOR)) {
            	PathMatcher matcher = matcherFromString(branch);
            	if (matcher!=null)
            		blacklistedBranches.add(matcher);
            }
        }
    }

    public boolean isValidBranch(String branch) {
    	Path branchAsPath = null;
    	
    	try{
    		branchAsPath = FileSystems.getDefault().getPath(branch);
    	}catch (InvalidPathException ipe){
    		LOGGER.error(String.format("Invalid branch name: %s", branch), ipe);
    	}
    	
    	return (branchAsPath!=null && isSelectedBranch(branchAsPath) && !isBlacklistedBranch(branchAsPath));
    }
    
    private PathMatcher matcherFromString(String path){
    	PathMatcher matcher = null;
    	
    	try{
    		matcher = FileSystems.getDefault().getPathMatcher(GLOB_PATTERN + path.trim());
    	}catch (IllegalArgumentException | UnsupportedOperationException exc){
    		LOGGER.error(String.format("Invalid glob pattern in branch name: %s", path), exc);
    	}
    	
    	return matcher;
    }
    
    private boolean isSelectedBranch(Path branch) {
    	
    	if (selectedBranches.size()==0)
    		return true;
    	
    	for (PathMatcher path : selectedBranches){
    		if (path.matches(branch))
    				return true;
    	}
    	
    	return false;
    }
    
    private boolean isBlacklistedBranch(Path branch) {
    	
    	if (blacklistedBranches.size()==0)
    		return false;
    	
    	for (PathMatcher path : blacklistedBranches){
    		if (path.matches(branch))
    				return true;
    	}
    	
    	return false;
    }

}