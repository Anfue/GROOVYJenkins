node {
    stage ('Borrar archivos inicales'){
    def remote = [:]
        remote.name = 'REGRE'
        remote.host = '10.17.32.123'
        remote.user = 'ocat'
        remote.password = 'ocat01'
        remote.allowAnyHosts = true
        //sshCommand remote: remote, command: 'whoami'
        def MY_FILE = remote fileExists '/tmp/myfile'
        //def exitCode = sshCommand remote: remote, command: 'find / -name "EDU_RULES.txt" | wc -l', returnStatus: true boolean exists = exitCode == 0
        def exists = fileExists remote '/usr/software/EDU_RULES'
        if (exists){
            echo 'YES'
            sshCommand remote: remote, command: 'cd /usr/software; rm -fr edu/; rm EDU_RULES.txt'
        }else{
            echo 'NO'
        }
    }
    stage ('Crear archivo'){
     remote = [:]
            remote.name = 'REGRE'
            remote.host = '10.17.32.123'
            remote.user = 'ocat'
            remote.password = 'ocat01'
            remote.allowAnyHosts = true
            //sshCommand remote: remote, command: 'whoami'
            sshCommand remote: remote, command: 'cd /usr/software; ls -rtlisa'
            sshCommand remote: remote, command: 'cd /usr/software; mkdir edu'
            sshCommand remote: remote, command: 'cd /usr/software/edu; touch EDU_RULES.txt'
    }stage ('copiar archivo'){
        remote = [:]
            remote.name = 'REGRE'
            remote.host = '10.17.32.123'
            remote.user = 'ocat'
            remote.password = 'ocat01'
            remote.allowAnyHosts = true
            when (true){
            sshCommand remote: remote, command: 'cd /usr/software; cp ./EDU_RULES.txt ./edu/'
            }
    }
}
