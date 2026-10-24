package org.example

class Utils implements Serializable {
    def steps

    Utils(steps) {
        this.steps = steps
    }

    String shortCommit() {
        return steps.sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
    }

    void notify(String message) {
        steps.echo "[NOTIFY] ${message}"
    }
}
