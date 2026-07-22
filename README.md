# PQC Demo Project

Qodana for JVM includes inspections that flag cryptographic code which should be
migrated to post-quantum cryptography (PQC).

This project is a minimal, self-contained example exercises signatures, hashes, key agreement, 
ciphers, TLS protocols, and custom providers so you can see exactly which calls each inspection level reports.

## References

- [Post-quantum cryptography (Wikipedia)](https://en.wikipedia.org/wiki/Post-quantum_cryptography)
- [NIST Post-Quantum Cryptography](https://www.nist.gov/pqc)

## Inspection levels

The inspections are grouped into five levels. Each level is cumulative — it includes
every level below it — and maps to a NIST PQC security level. Higher levels are
stricter and report more code.

| Level          | Meaning                                                          |
|----------------|------------------------------------------------------------------|
| `PqcMinLevel1` | Informational recommendations for future PQC migration.          |
| `PqcMinLevel2` | Recommend beginning migration planning. Includes Level 1.        |
| `PqcMinLevel3` | Recommend active migration to PQC. Includes Levels 1–2.          |
| `PqcMinLevel4` | Flag cryptography nearing the end of its recommended use. Includes Levels 1–3. |
| `PqcMinLevel5` | Flag cryptography that should no longer be used. Includes Levels 1–4. |

## Configuration

One level is enabled at a time in [`qodana.yaml`](qodana.yaml). To switch levels,
change the `group` value to the level you want (`PqcMinLevel1` through
`PqcMinLevel5`):

```yaml
version: "1.0"
profile:
  name: qodana.recommended
  inspections:
    # Choose the PQC inspection level below. Each level is cumulative and includes
    # every level below it:
    #   PqcMinLevel1 – Informational recommendations for future PQC migration.
    #   PqcMinLevel2 – Recommend beginning migration planning. Includes Level 1.
    #   PqcMinLevel3 – Recommend active migration to PQC. Includes Levels 1–2.
    #   PqcMinLevel4 – Flag cryptography nearing the end of its recommended use. Includes Levels 1–3.
    #   PqcMinLevel5 – Flag cryptography that should no longer be used. Includes Levels 1–4.
    - group: PqcMinLevel5
      enabled: true
```

## Running the analysis

Run the Qodana JVM linter with Docker from the project root:

```bash
docker run --rm -it \
    -p 8080:8080 \
    -e QODANA_TOKEN="$QODANA_TOKEN" \
    -v "$(pwd)":/data/project/ \
    -v "$(pwd)/.qodana/results":/data/results \
    registry.jetbrains.team/p/sa/containers/qodana-jvm:latest \
    --show-report
```