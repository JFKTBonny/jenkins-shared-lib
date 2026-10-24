# Jenkins Shared Library

Trusted shared library for Jenkins pipelines, registered as **my-shared-lib** by JCasC in the `jenkins-platform` repo (`controller/casc/unclassified.yaml`).

> This code runs **outside the Groovy sandbox**. Only admins may push to this repo. Protect `main` with required reviews.

## Layout

```text
jenkins-shared-lib/
├── vars/
│   ├── buildApp.groovy      # buildApp(name:, lang:)
│   ├── buildApp.txt         # help text shown in Jenkins UI
│   └── k8sPod.groovy        # k8sPod() -> restricted pod YAML
├── src/
│   └── org/example/
│       └── Utils.groovy     # shortCommit(), notify()
└── resources/
    └── org/example/
        └── pod-template.yaml
```

## Steps

| Step | Description |
|---|---|
| `buildApp(name: 'x', lang: 'node')` | `npm ci && npm run build` |
| `buildApp(name: 'x', lang: 'maven')` | `mvn -B clean package` |
| `k8sPod()` | Returns the restricted Kubernetes pod spec |

## Usage

```groovy
@Library('my-shared-lib') _
import org.example.Utils

pipeline {
    agent { label 'linux' }
    stages {
        stage('Build') {
            steps {
                script {
                    def utils = new Utils(this)
                    echo "Commit: ${utils.shortCommit()}"
                    buildApp(name: 'frontend', lang: 'node')
                    utils.notify('Build finished')
                }
            }
        }
    }
}
```

Kubernetes pod from the library:

```groovy
@Library('my-shared-lib') _

pipeline {
    agent {
        kubernetes {
            yaml k8sPod()
            defaultContainer 'build'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean package'
            }
        }
    }
}
```

## Releasing

Tag releases so projects can pin them (requires `allowVersionOverride: true` in JCasC):

```bash
git tag v1.0.0
git push origin v1.0.0
```

```groovy
@Library('my-shared-lib@v1.0.0') _
```

## Push to GitHub

```bash
git init -b main
git add .
git commit -m "Initial shared library"
gh repo create your-org/jenkins-shared-lib --private --source=. --push
```

Then on GitHub: **Settings → Branches → Add rule** for `main`: require a pull request, 1 approval, and restrict pushes to admins.
