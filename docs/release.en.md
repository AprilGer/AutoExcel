# Maven Central Release

1. Configure Sonatype credentials in `settings.xml`.
2. Configure GPG key for signing.
3. Run:

```bash
mvn clean deploy -Prelease
```

4. Close and release staging repository in Sonatype portal.
