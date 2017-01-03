package grails.plugin.coveralls.git

class GitInfoFactory {

    def GitReport createReport() {
        def gitCmds = [
                head   : [
                        id              : 'rev-parse HEAD',
                        author_name     : 'log -1 --pretty=%aN',
                        author_email    : 'log -1 --pretty=%aE',
                        committer_name  : 'log -1 --pretty=%cN',
                        committer_email : 'log -1 --pretty=%cE',
                        message         : 'log -1 --pretty=%s'
                ],
                branch : 'rev-parse --abbrev-ref HEAD',
                remotes: 'remote -v',
        ]
        def branch = "git ${gitCmds['branch']}".execute().getText().trim()

        Head headReport = new Head()
        gitCmds['head'].each { key, command ->
            headReport."${key}" = "git ${command}".execute().getText().trim()
        }

        GitReport gitReport = new GitReport(head: headReport, branch: branch, remotes: [])

        def remotes = "git ${gitCmds['remotes']}".execute().getText().split()
        def total = (remotes.size()/3)-1
        ((0..total).collect{it*3}).each { i ->
            String name = remotes[i].trim()
            String url = remotes[i+1].trim()
            Remotes remoteReport = new Remotes(name: name, url: url)
            gitReport.remotes << remoteReport
        }

        return gitReport
    }
}
