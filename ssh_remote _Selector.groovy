parameters([
        choice(name: 'ENTORNO', choices: 'JENKINS_TEST\nREGRE\nTEST\nTEST2\nDESA\nDESA2\nPROD', description: 'some parameter'),
        string(name : "VERSION", defaultValue: "XXXXX", description: 'poner version de BBDD')
    ])
def IP = 0
def IPBD = 0
def nameBD = 'XXX'
if (ENTORNO == 'REGRE'){
    IP = '10.17.32.123'
    nameBD = 'REGRE_BBDD(123)'
}else if (ENTORNO == 'JENKINS_TEST'){
    IP = '10.17.32.233'
}else if (ENTORNO == 'TEST'){
    IP = '10.17.32.232'
    IPBD = '10.17.32.231'
    nameBD = 'TEST(231)'
}else if (ENTORNO == 'TEST2'){
    IP = '10.17.32.121'
    IPBD = '10.17.32.216'
    nameBD = 'TEST2(216)'
}else if (ENTORNO == 'DESA'){
    IP = '10.17.32.239'
    IPBD = '10.17.32.237'
    nameBD = 'DESA(237)'
}else if (ENTORNO == 'DESA2'){
    IP = '10.17.32.120'
    IPBD = '10.17.32.226'
    nameBD = 'DESA2(226)'
}else if (ENTORNO == 'PPROD'){
    IP = '10.17.32.204'
    IPBD = '10.17.32.205'
    nameBD = 'PROD(205)'
}
def remote = [:]
remote.name = ENTORNO
remote.host = IP
remote.user = 'ocat'
remote.password = 'ocat01'
remote.allowAnyHosts = true

def remoteBD = [:]
remoteBD.name = ENTORNO
remoteBD.host = IPBD
remoteBD.user = 'oracle'
remoteBD.password = 'oracle'
remoteBD.allowAnyHosts = true

node{
    deleteDir()
    def mvnHome
    stage ('Initialize') {
        println '<------'
        sh 'pwd'
        sh '''
                            echo "PATH = ${PATH}"
                            echo "MAVEN_HOME = ${MAVEN_HOME}"
                        '''
        println '------>'
    }
    stage ('Parar colas y front end'){
        if (ENTORNO == 'PROD'){
            sh 'echo "No se trabaja en PROD"'
        }else{
            sh "echo 'Entotno-->${ENTORNO}'"
            sh "echo 'Entotno-->${IP}'"
            sh "echo 'Para colas'"
            
            //remote.sudo = true
            /*
            sshCommand remote: remote, command: "cd /data/elena/installer/"
            sshCommand remote: remote, command: '/data/elena/installer/ggccInstallManager.sh --stop ${ENTORNO} GGCC-ELENAJMS
            sshCommand remote: remote, command: '/data/elena/installer/ggccInstallManager.sh --stop ${ENTORNO} GGCC-FRONTEND'
            sshCommand remote: remote, command: '/data/elena/installer/ggccInstallManager.sh --stop ${ENTORNO} GGCC-JS'
             */
            //sh "echo 'Entotno-->${ENTORNO}'"
        }
    }
    stage('Instalador'){
        sshCommand remote: remote, command: "cd /data/elena/installer/"
        /*
        cd /data/elena/installer/
echo "ocat01" | ./ggccInstallManager.sh --undeploy PROD GGCC-FRONTEND
echo "ocat01" | ./ggccInstallManager.sh --undeploy PROD GGCC-JS
echo "ocat01" | ./ggccInstallManager.sh --install PROD GGCC-ADMINSID ${VERSION_RELEASE} NEXUS
echo "ocat01" | ./ggccInstallManager.sh --install PROD GGCC-ELENAJMS ${VERSION_RELEASE} NEXUS
echo "ocat01" | ./ggccInstallManager.sh --install PROD GGCC-FRONTEND ${VERSION_RELEASE} NEXUS
echo "ocat01" | ./ggccInstallManager.sh --install PROD GGCC-JS ${VERSION_RELEASE} NEXUS
echo "ocat01" | ./ggccInstallManager.sh --install PROD GGCC-SENDMAIL ${VERSION_RELEASE} NEXUS
         * */
    }
    stage('Descarga SVN BBDD') {
        checkout poll: "H/5 * * * *", scm:
        (
            [$class: 'SubversionSCM',
                additionalCredentials: [
                ], excludedCommitMessages: '', 
                excludedRegions: '', 
                excludedRevprop: '', 
                excludedUsers: '', 
                filterChangelog: false, 
                ignoreDirPropChanges: false, 
                includedRegions: '', 
                locations: [
                    [cancelProcessOnExternalsFail: true, 
                        credentialsId: '7d98d430-ff46-4cb1-93f5-06c38f39ecc8', 
                        depthOption: 'infinity', 
                        ignoreExternalsOption: true, 
                        local: '.', 
                        remote: 'https://10.17.32.98:8443/svn/BTools/elena/trunk/DataBase/PaP/Elena']
                ], 
                quietOperation: true, 
                workspaceUpdater: [
                    $class: 'UpdateUpdater']
            ])
    
        // Get the Maven tool.
        mvnHome = tool 'mvn.3.6.0'
        println mvnHome
        sh "echo 'DESCARGADO ARCHIVOS'"
           
    }
    stage ('Borrar carpeta PaP de BD'){
        if (ENTORNO == 'PROD'){
            sh 'echo "No se trabaja en PROD"'
        }else{
            sshCommand remote: remoteBD, command: 'rm -fr /usr/local/Jenkins/PaP/*'    
            sh "echo 'BORRADO DE /usr/local/Jenkins/PaP/'"
        }
    }
    stage ('Transferir archivos nuevos a carpeta PaP de BD'){
        sshPublisher(
            failOnError: true,
            publishers: [
                sshPublisherDesc(
                    configName: nameBD,
                    sshCredentials: [
                        username: 'oracle',
                        encryptedPassphrase: "oracle"
                    ], 
                    transfers: [
                        sshTransfer(
                            sourceFiles: '*/**',
                        )
                    ]
                )
            ]
        )
         
    }
    stage ('Ejecuta SH BD'){
        sshCommand remote: remoteBD, command: "/usr/local/Jenkins/lanzar_scripts_BBDD.sh ${VERSION}"
    }
    stage ('Levantar colas'){
        if (ENTORNO == 'PROD'){
            sh 'echo "No se trabaja en PROD"'
        }else{
            sh "echo 'Entotno-->${ENTORNO}'"
            sh "echo 'Levanta colas'"
            
            //remote.sudo = true
            /*
            sshCommand remote: remote, command: "cd /data/elena/installer/"
            sshCommand remote: remote, command: '/data/elena/installer/ggccInstallManager.sh --start ${ENTORNO} GGCC-ELENAJMS
            sshCommand remote: remote, command: '/data/elena/installer/ggccInstallManager.sh --start ${ENTORNO} GGCC-FRONTEND'
            sshCommand remote: remote, command: '/data/elena/installer/ggccInstallManager.sh --start ${ENTORNO} GGCC-JS'
             */
            //sh "echo 'Entotno-->${ENTORNO}'"
        }
    }
}