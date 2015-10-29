package in.ashwanthkumar.gocd.github.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BranchFilterUtilTest {

	@Test
    public void acceptAllBranchAsDefault() {
        BranchFilter blacklistUtil = new BranchFilter("","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
    }
	
	@Test
    public void acceptAllBranchWithGlob() {
        BranchFilter blacklistUtil = new BranchFilter("*","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
    }
	
	@Test
    public void rejectAllBranchWithGlob() {
        BranchFilter blacklistUtil = new BranchFilter("","*");

        assertFalse(blacklistUtil.isValidBranch("branch1"));
        assertFalse(blacklistUtil.isValidBranch("branch2"));
    }
	
    @Test
    public void acceptSingleBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch1","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertFalse(blacklistUtil.isValidBranch(""));
        assertFalse(blacklistUtil.isValidBranch("branch2"));
    }
    
    @Test
    public void acceptSingleGlobBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch*","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertTrue(blacklistUtil.isValidBranch("branch name"));
        assertTrue(blacklistUtil.isValidBranch("branch"));
        assertFalse(blacklistUtil.isValidBranch("otherbranch"));
    }
    
    @Test
    public void acceptSingleCharacterGlobBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch?","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertFalse(blacklistUtil.isValidBranch("branch22"));
        assertFalse(blacklistUtil.isValidBranch("branch"));
        assertFalse(blacklistUtil.isValidBranch("1branch"));
        assertFalse(blacklistUtil.isValidBranch("otherbranch"));
    }
    
    
    @Test
    public void acceptNameAsDirectoryGlobBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch/*","");

        assertTrue(blacklistUtil.isValidBranch("branch/1"));
        assertTrue(blacklistUtil.isValidBranch("branch/2"));
        assertTrue(blacklistUtil.isValidBranch("branch/name"));
        assertFalse(blacklistUtil.isValidBranch("branch/other/name"));
        assertFalse(blacklistUtil.isValidBranch("branch"));        
    }
    
    @Test
    public void acceptNameAsCrossingDirectoryGlobBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch/**","");

        assertTrue(blacklistUtil.isValidBranch("branch/1"));
        assertTrue(blacklistUtil.isValidBranch("branch/2"));
        assertTrue(blacklistUtil.isValidBranch("branch/name"));
        assertTrue(blacklistUtil.isValidBranch("branch/other/name"));
        assertFalse(blacklistUtil.isValidBranch("branch"));        
    }
    
    @Test
    public void acceptOptionalSingleCharGlobBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch[12]","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertFalse(blacklistUtil.isValidBranch("branch"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void acceptOptionalSingleCharRangeNameGlobBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch[1-3]","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertTrue(blacklistUtil.isValidBranch("branch3"));
        assertFalse(blacklistUtil.isValidBranch("branch"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void acceptNegatieveOptionalSingleCharNameGlobBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch[!1-3]","");

        assertFalse(blacklistUtil.isValidBranch("branch1"));
        assertFalse(blacklistUtil.isValidBranch("branch2"));
        assertFalse(blacklistUtil.isValidBranch("branch3"));
        assertTrue(blacklistUtil.isValidBranch("branch4"));
        assertFalse(blacklistUtil.isValidBranch("branch"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    
    @Test
    public void acceptOptionalNameGlobBranch() {
        BranchFilter blacklistUtil = new BranchFilter("branch-{one,two}","");

        assertTrue(blacklistUtil.isValidBranch("branch-one"));
        assertTrue(blacklistUtil.isValidBranch("branch-two"));
        assertFalse(blacklistUtil.isValidBranch("branchone"));
        assertFalse(blacklistUtil.isValidBranch("branch-"));
        assertFalse(blacklistUtil.isValidBranch("branch"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void acceptMultipleBranches() {
        BranchFilter blacklistUtil = new BranchFilter("branch1,branch2","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void acceptMultipleGlobBranches() {
        BranchFilter blacklistUtil = new BranchFilter("branch*,*other*","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertTrue(blacklistUtil.isValidBranch("another"));
        assertTrue(blacklistUtil.isValidBranch("other branch"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }

    @Test
    public void trimBranchNames() {
        BranchFilter blacklistUtil = new BranchFilter(" branch1 , branch2   ,branch3,branch4  ","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertTrue(blacklistUtil.isValidBranch("branch3"));
        assertTrue(blacklistUtil.isValidBranch("branch4"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void trimGlobBranchNames() {
        BranchFilter blacklistUtil = new BranchFilter(" *branch1* , branch2*   ,branch?,branch4?  ","");

        assertTrue(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("one-branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertTrue(blacklistUtil.isValidBranch("one-branch123"));
        assertTrue(blacklistUtil.isValidBranch("branch3"));
        assertTrue(blacklistUtil.isValidBranch("branch41"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void rejectSingleBlacklistedBranch() {
        BranchFilter blacklistUtil = new BranchFilter("","branch1");

        assertFalse(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("master"));
    }

    @Test
    public void rejectMultipleBlacklistedBranches() {
        BranchFilter blacklistUtil = new BranchFilter("","branch1,branch2");

        assertFalse(blacklistUtil.isValidBranch("branch1"));
        assertFalse(blacklistUtil.isValidBranch("branch2"));
        assertTrue(blacklistUtil.isValidBranch("master"));
    }

    @Test
    public void shouldTrimBlacklistedBranchNames() {
        BranchFilter blacklistUtil = new BranchFilter(""," branch1 , branch2   ,branch3,branch4  ");

        assertFalse(blacklistUtil.isValidBranch("branch1"));
        assertFalse(blacklistUtil.isValidBranch("branch2"));
        assertFalse(blacklistUtil.isValidBranch("branch3"));
        assertFalse(blacklistUtil.isValidBranch("branch4"));
        assertTrue(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void rejectBranchSelectedAndBlacklisted() {
        BranchFilter blacklistUtil = new BranchFilter("branch1","branch1");

        assertFalse(blacklistUtil.isValidBranch("branch1"));
        assertFalse(blacklistUtil.isValidBranch("branch2"));
    }
    
    @Test
    public void multipleBranchSelectedAndBlacklisted() {
        BranchFilter blacklistUtil = new BranchFilter("branch1,branch2","branch1");

        assertFalse(blacklistUtil.isValidBranch("branch1"));
        assertTrue(blacklistUtil.isValidBranch("branch2"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void multipleGlobBranchSelectedAndBlacklisted() {
        BranchFilter blacklistUtil = new BranchFilter("branch/1/*,branch/2/*","branch/1/*");

        assertFalse(blacklistUtil.isValidBranch("branch/1/1"));
        assertTrue(blacklistUtil.isValidBranch("branch/2/1"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
    
    @Test
    public void multipleGlobSubPatternsSelectedAndBlacklisted() {
        BranchFilter blacklistUtil = new BranchFilter("{branch1,branch2}/*","branch3/*,master");

        assertTrue(blacklistUtil.isValidBranch("branch1/1"));
        assertTrue(blacklistUtil.isValidBranch("branch2/2"));
        assertFalse(blacklistUtil.isValidBranch("branch3/3"));
        assertFalse(blacklistUtil.isValidBranch("master"));
    }
}