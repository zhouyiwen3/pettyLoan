package com.ps.config;

        import org.apache.zookeeper.KeeperException;
        import org.kohsuke.github.GHRepository;
        import org.kohsuke.github.GitHub;

        import java.io.IOException;

/**
 * @author zzz
 * @date 2019/7/15 19:21
 */
public class Demo {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        GitHub gitHub = GitHub.connect();
        GHRepository repo = gitHub.createRepository("new-repository", "this is my new repository",
                "https://api.github.com/users/zhouyiwen3",true);
        //  http://www.kohsuke.org/

        repo.addCollaborators(gitHub.getUser("zhouyiwen3"));

    }

}
