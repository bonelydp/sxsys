# Bioinformatics Data

## References

- `references/cryptosporidium-18s-reference.fasta`
  - Source file: `Cry-18S-ref-seq-2025-03-17-updated.fas`
  - Reference date: 2025-03-17
- `references/cryptosporidium-gp60-reference.fasta`
  - Source file: `Cry-GP60-2025-03-17-updated.fas`
  - Reference date: 2025-03-17

## BLAST databases

`blast_db/` is reserved for indexes generated from the reference FASTA files by
NCBI `makeblastdb`. Generated indexes should use stable prefixes:

- `cryptosporidium-18s`
- `cryptosporidium-gp60`

Generate or refresh both databases with:

```bash
docker compose --profile tools run --rm bio-db-init
```
