import os
import time
from threading import Thread

def elettore():
    cmd_elettore = 'java -Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=changeit  it.unisa.diem.aps.aps_maninthemiddle.ElettoreMain'
    os.system('cmd /c ' + cmd_elettore)

def presidente():

    cmd_pres = 'java -Djavax.net.ssl.keyStore=keystore.jks -Djavax.net.ssl.keyStorePassword=changeit  -Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=changeit  it.unisa.diem.aps.aps_maninthemiddle.PresidenteMain'
    os.system('cmd /c ' + cmd_pres)

""" COMPILING  """
os.chdir('src/main/java')
os.system('cmd /c ' + 'javac it/unisa/diem/aps/aps_maninthemiddle/Utils.java')
os.system('cmd /c ' + 'javac it/unisa/diem/aps/aps_maninthemiddle/ElettoreMain.java')
os.system('cmd /c ' + 'javac it/unisa/diem/aps/aps_maninthemiddle/PresidenteMain.java')


project_dir = 'src/main/java/it/unisa/diem/aps/aps_maninthemiddle'
print(os.listdir())

#print(os.listdir(project_dir))

cmd_keyTool = 'keytool  -genkey -noprompt -trustcacerts -keyalg RSA -alias ssltest  -dname "cn=localhost, ou=DIEM, o=unisa, c=IT" -keypass changeit -keystore keystore.jks'
cmd_export = 'keytool -export -alias ssltest -storepass changeit -file sslserver.cer -keystore keystore.jks'
cmd_import = 'keytool -import -v -trustcacerts -alias ssltest -keystore truststore.jks -file sslserver.cer -keypass changeit'
cmd_genkey = 'keytool  -genkey -noprompt -trustcacerts -keyalg ec -keysize 256 -sigalg SHA256withECDSA -alias ssltest  -dname "cn=localhost, ou=DIEM, o=unisa, c=IT" -keypass changeit -keystore eckeystore.jks'

os.system('cmd /c ' + cmd_keyTool)
os.system('cmd /c ' + cmd_export)
os.system('cmd /c ' + cmd_import)
os.system('cmd /c ' + cmd_genkey)

#os.chdir('../../../../..')
print(os.listdir())
e = Thread(target=elettore)
e1 = Thread(target=elettore)
p = Thread(target=presidente)

p.start()
time.sleep(1)
e.start()
print('mario')
e1.start()




#os.system('cmd /c "changeit"')
