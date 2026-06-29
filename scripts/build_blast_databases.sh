#!/usr/bin/env bash
set -euo pipefail

REFERENCE_DIR="${BIO_REFERENCE_DIR:-/app/bio-data/references}"
DATABASE_DIR="${BIO_DATABASE_DIR:-/app/bio-data/blast_db}"

build_database() {
    local source_file="$1"
    local database_prefix="$2"
    local title="$3"

    if [[ ! -s "${source_file}" ]]; then
        echo "Reference FASTA not found or empty: ${source_file}" >&2
        exit 1
    fi

    mkdir -p "${DATABASE_DIR}"
    rm -f "${database_prefix}".*

    makeblastdb \
        -in "${source_file}" \
        -dbtype nucl \
        -title "${title}" \
        -out "${database_prefix}"

    blastdbcmd -db "${database_prefix}" -info
}

build_database \
    "${REFERENCE_DIR}/cryptosporidium-18s-reference.fasta" \
    "${DATABASE_DIR}/cryptosporidium-18s" \
    "Cryptosporidium 18S reference sequences"

build_database \
    "${REFERENCE_DIR}/cryptosporidium-gp60-reference.fasta" \
    "${DATABASE_DIR}/cryptosporidium-gp60" \
    "Cryptosporidium GP60 reference sequences"

echo "BLAST databases generated successfully in ${DATABASE_DIR}"
