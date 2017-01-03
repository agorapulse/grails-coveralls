package grails.plugin.coveralls.git

import groovy.json.JsonBuilder

class GitReport {
    Head head
    String branch
    List<Remotes> remotes

    public String toString() {
        JsonBuilder json = new JsonBuilder(this)
        return json.toString()
    }
}

class Head {
    String id
    String author_name
    String author_email
    String committer_name
    String committer_email
    String message
}

class Remotes {
    String name
    String url
}
