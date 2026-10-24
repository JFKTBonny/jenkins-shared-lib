// Global step: buildApp(name: 'frontend', lang: 'node')
def call(Map config = [:]) {
    def name = config.get('name', 'app')
    def lang = config.get('lang', 'node')

    echo "Building ${name} (${lang})"

    if (lang == 'node') {
        sh 'npm ci && npm run build'
    } else if (lang == 'maven') {
        sh 'mvn -B clean package'
    } else {
        error "Unsupported language: ${lang}"
    }
}
