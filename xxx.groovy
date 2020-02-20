def remote = [:]
remote.name = 'XXX'
remote.host = 'XX.XX.XX.XXX'
remote.user = 'ocat'
remote.password = 'ocat01'
remote.allowAnyHosts = true

node{
    parameters([
            choice(name: 'key1', choices: 'JENKINS_TEST\nREGRE\nTEST\nTEST2\nDESA\nDESA2\nPROD', description: 'some parameter')
        ])
    stage ('Parar colas'){
        println key1
        sh "echo '${key1}'"
        
        switch ('${key1}'){
        case 'JENKINS_TEST':
            remote = [:]
            remote.name = 'JENKINS_TEST'
            remote.host = '10.17.32.233'
            remote.user = 'ocat'
            remote.password = 'ocat01'
            remote.allowAnyHosts = true
            sshCommand remote: remote, command: "ls -lrt"
            sshCommand remote: remote, command: "for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done"
            
        case 'REGRE':
            remote = [:]
            remote.name = 'REGRE'
            remote.host = '10.17.32.123'
            remote.user = 'ocat'
            remote.password = 'ocat01'
            sshCommand remote: remote, command: "ls -lrt"
            sshCommand remote: remote, command: "for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done"
            
            
        case 'TEST':
            remote = [:]
            remote.name = 'TEST'
            remote.host = '10.17.32.232'
            sshCommand remote: remote, command: "cd /usr/software"
            archive includes: 'Edu_Mola'
            writeFile file: 'testT.sh', text: 'ls'
            sshCommand remote: remote, command: "pwd"
            sshCommand remote: remote, command: "echo 'ocat01' | mv ./test.sh /usr/software"
            sshCommand remote: remote, command: 'for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done'
            sshScript remote: remote, script: 'testT.sh'
            sshPut remote: remote, from: 'testT.sh', into: '.'
            sshGet remote: remote, from: 'testT.sh', into: 'test_new.sh', override: true
            sh 'ls -l'
            
        case 'TEST2':
            remote = [:]
            remote.name = 'TEST2'
            remote.host = '10.17.32.121'
            sshCommand remote: remote, command: "cd /usr/software"
            writeFile file: 'testT.sh', text: 'ls'
            sshCommand remote: remote, command: "pwd"
            sshCommand remote: remote, command: "echo 'ocat01' | mv ./test.sh /usr/software"
            sshCommand remote: remote, command: 'for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done'
            sshScript remote: remote, script: 'testT.sh'
            sshPut remote: remote, from: 'testT.sh', into: '.'
            sshGet remote: remote, from: 'testT.sh', into: 'test_new.sh', override: true
            //sshCommand remote: remote, command: "echo 'ocat01' | mv ./testT.sh /usr/software"
            //sshRemove remote: remote, path: 'test.sh'
            
        case 'DESA':
            remote = [:]
            remote.name = 'DESA'
            remote.host = '10.17.32.239'
            sshCommand remote: remote, command: "cd /usr/software"
            writeFile file: 'testT.sh', text: 'ls'
            sshCommand remote: remote, command: "pwd"
            sshCommand remote: remote, command: "echo 'ocat01' | mv ./test.sh /usr/software"
            sshCommand remote: remote, command: 'for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done'
            sshScript remote: remote, script: 'testT.sh'
            sshPut remote: remote, from: 'testT.sh', into: '.'
            sshGet remote: remote, from: 'testT.sh', into: 'test_new.sh', override: true
            //sshCommand remote: remote, command: "echo 'ocat01' | mv ./testT.sh /usr/software"
            //sshRemove remote: remote, path: 'test.sh'
            
        case 'DESA2':
            remote = [:]
            remote.name = 'DESA'
            remote.host = '10.17.32.120'
            sshCommand remote: remote, command: "cd /usr/software"
            writeFile file: 'testT.sh', text: 'ls'
            sshCommand remote: remote, command: "pwd"
            sshCommand remote: remote, command: "echo 'ocat01' | mv ./test.sh /usr/software"
            sshCommand remote: remote, command: 'for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done'
            sshScript remote: remote, script: 'testT.sh'
            sshPut remote: remote, from: 'testT.sh', into: '.'
            sshGet remote: remote, from: 'testT.sh', into: 'test_new.sh', override: true
            //sshCommand remote: remote, command: "echo 'ocat01' | mv ./testT.sh /usr/software"
            //sshRemove remote: remote, path: 'test.sh'
            
        case 'PROD':
            remote = [:]
            remote.name = 'PROD'
            remote.host = '10.17.32.204'
            sshCommand remote: remote, command: "cd /usr/software"
            writeFile file: 'testT.sh', text: 'ls'
            sshCommand remote: remote, command: "pwd"
            sshCommand remote: remote, command: "echo 'ocat01' | mv ./test.sh /usr/software"
            sshCommand remote: remote, command: 'for i in {1..5}; do echo -n \"Loop \$i \"; date ; sleep 1; done'
            sshScript remote: remote, script: 'testT.sh'
            sshPut remote: remote, from: 'testT.sh', into: '.'
            sshGet remote: remote, from: 'testT.sh', into: 'test_new.sh', override: true
            //sshCommand remote: remote, command: "echo 'ocat01' | mv ./testT.sh /usr/software"
            //sshRemove remote: remote, path: 'test.sh'
        }
    }
    stage ('QUE ENTORNO ES'){
        println key1
        sh "echo 'Entorno --> ${key1}'"
    }
    /*stage ('Levantar Colas'){
    switch ('${key1}'){
    case 'JENKINS_TEST':
    remote = [:]
    remote.name = 'JENKINS_TEST'
    remote.host = '10.17.32.233'
    case 'REGRE':
    remote = [:]
    remote.name = 'REGRE'
    remote.host = '10.17.32.123'
    sshCommand remote: remote, command: "cd /data/elena/installer/"
    //sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    //sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    case 'TEST':
    remote = [:]
    remote.name = 'TEST'
    remote.host = '10.17.32.232'        
    sshCommand remote: remote, command: "cd /data/elena/installer/"
    sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    //sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    //sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    case 'TEST2':
    remote = [:]
    remote.name = 'TEST2'
    remote.host = '10.17.32.121'
    sshCommand remote: remote, command: "cd /data/elena/installer/"
    sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    //sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    //sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    case 'DESA':
    remote = [:]
    remote.name = 'DESA'
    remote.host = '10.17.32.239'        
    sshCommand remote: remote, command: "cd /data/elena/installer/"
    sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    //sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    //sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    case 'DESA2':
    remote = [:]
    remote.name = 'DESA2'
    remote.host = '10.17.32.120'        
    sshCommand remote: remote, command: "cd /data/elena/installer/"
    sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    sshCommand remote: remote, command: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    //sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-FRONTEND'
    //sshScript remote: remote, script: 'echo "ocat01" |  ./ggccInstallManager.sh --start TEST GGCC-JS'
    }
    }
     */
        
}

