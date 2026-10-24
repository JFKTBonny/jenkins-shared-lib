// Returns the restricted pod spec bundled with this library.
// Usage: agent { kubernetes { yaml k8sPod(); defaultContainer 'build' } }
def call() {
    return libraryResource('org/example/pod-template.yaml')
}
