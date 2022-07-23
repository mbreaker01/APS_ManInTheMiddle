import os
import time
from threading import Thread

def elettore():
    cmd_elettore = 'java -Djavax.net.ssl.keyStore=keystore.jks -Djavax.net.ssl.keyStorePassword=changeit ElettoreMain.java'
    os.system('cmd /c ' + cmd_elettore)

def presidente():
    cmd_pres = 'java -Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=changeit PresidenteMain.java'
    os.system('cmd /c ' + cmd_pres)

project_dir = 'src/main/java/java/it/unisa/diem/aps/aps_maninthemiddle'
os.chdir(project_dir)
print(os.listdir())

#print(os.listdir(project_dir))

cmd_keyTool = '"keytool  -genkey -noprompt -trustcacerts -keyalg RSA -alias ssltest  -dname "cn=localhost, ou=DIEM, o=unisa, c=IT" -keypass changeit -keystore keystore.jks"'
cmd_export = 'keytool -export -alias ssltest -storepass changeit -file sslserver.cer -keystore keystore.jks'
cmd_import = 'keytool -import -v -trustcacerts -alias ssltest -keystore truststore.jks -file sslserver.cer -keypass changeit'

os.system('cmd /c ' + cmd_keyTool)
os.system('cmd /c ' + cmd_export)
os.system('cmd /c ' + cmd_import)

os.system('cmd /c  cd ' + project_dir)
print(os.listdir())
e = Thread(target=elettore)
p = Thread(target=presidente)

p.start()
time.sleep(1)
e.start()



#os.system('cmd /c "changeit"')
