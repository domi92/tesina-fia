# Tesina FIA
## Requisiti
- jdk 1.6
- [netbeans 7.0.1](https://netbeans.org/)
- [jgrapht 0.8.3](https://github.com/jgrapht/jgrapht) (inclusa nel repository in `third-party-libs`)

## Compilazione
Si può compilare ed eseguire da netbeans aprendo come un progetto la cartella `tesina-fia`

In alternativa è possibile compilare con ant
```
$ cd /path/to/tesina-fia
$ ant -f nbbuild.xml compile
```
Per eseguire l'applicazione si può usare sempre ant
```
$ ant -f nbbuild.xml run
```
oppure si può eseguire direttamente il jar
```
$ java -jar dist/Tesina_FIA.jar
```

Le librerie di terze parti vengono cercate dal compilatore nella cartella 
`third-party-libs`, se è necessario aggiungerne altre vanno specificate in
`nbproject/project.properties`. Si può anche fare da netbeans senza modificare
manualmente il file.
