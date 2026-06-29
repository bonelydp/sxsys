#!/usr/bin/env python3
import http.client
import json
import urllib.parse
import urllib.request
import uuid


BASE_URL = "http://127.0.0.1:8086"


def request_form(path, fields, timeout=300):
    data = urllib.parse.urlencode(fields).encode()
    request = urllib.request.Request(
        BASE_URL + path,
        data=data,
        headers={"Content-Type": "application/x-www-form-urlencoded"},
    )
    with urllib.request.urlopen(request, timeout=timeout) as response:
        return response.status, response.read(), dict(response.headers)


def request_multipart(path, fields, file_field, filename, content, timeout=600):
    boundary = "----shouyi-" + uuid.uuid4().hex
    body = bytearray()

    for name, value in fields.items():
        body.extend(f"--{boundary}\r\n".encode())
        body.extend(f'Content-Disposition: form-data; name="{name}"\r\n\r\n'.encode())
        body.extend(str(value).encode())
        body.extend(b"\r\n")

    body.extend(f"--{boundary}\r\n".encode())
    body.extend(
        f'Content-Disposition: form-data; name="{file_field}"; '
        f'filename="{filename}"\r\n'.encode()
    )
    body.extend(b"Content-Type: application/octet-stream\r\n\r\n")
    body.extend(content)
    body.extend(b"\r\n")
    body.extend(f"--{boundary}--\r\n".encode())

    connection = http.client.HTTPConnection("127.0.0.1", 8086, timeout=timeout)
    connection.request(
        "POST",
        path,
        body=bytes(body),
        headers={"Content-Type": f"multipart/form-data; boundary={boundary}"},
    )
    response = connection.getresponse()
    data = response.read()
    headers = dict(response.getheaders())
    connection.close()
    if response.status >= 400:
        raise RuntimeError(f"{path} returned {response.status}: {data.decode(errors='replace')}")
    return response.status, data, headers


def assert_nonempty(label, data):
    if not data:
        raise RuntimeError(f"{label} returned an empty response")
    print(f"{label}: OK ({len(data)} bytes)")


def main():
    sequence_id = "MW075513_Cryptosporidium_abrahamseni"
    encoded = urllib.parse.urlencode(
        {
            "sequenceName": sequence_id,
            "database": "18S-参考基因组-2024-11-24",
        }
    )
    with urllib.request.urlopen(f"{BASE_URL}/sequence?{encoded}", timeout=60) as response:
        sequence = response.read()
    assert_nonempty("Sequence lookup", sequence)
    query = ">smoke_test\n" + sequence.decode() + "\n"

    _, blast_result, _ = request_form(
        "/runBlast",
        {
            "querySequence": query,
            "selectedDatabase": "18S-参考基因组-2024-11-24",
            "jobTitle": "smoke-test",
        },
    )
    assert_nonempty("BLAST", blast_result)

    _, mafft_result, _ = request_form(
        "/runMafft",
        {
            "querySequence": sequence.decode(),
            "selectedDataBase": "18S",
            "experimentName": "smoke-test",
        },
        timeout=600,
    )
    assert_nonempty("MAFFT", mafft_result)

    aligned = (
        b">A\nACGTACGTACGTACGTACGT\n"
        b">B\nACGTACGTACGTACGTACGA\n"
        b">C\nACGTACGTACGTACGTTCGA\n"
        b">D\nACGTACGTACGTTCGTTCGA\n"
        b">E\nACGTACGTTCGTTCGTTCGA\n"
    )
    _, tree_file, _ = request_multipart(
        "/iqtree",
        {"experimentName": "smoke-test"},
        "alignedFile",
        "aligned.fasta",
        aligned,
        timeout=600,
    )
    assert_nonempty("IQ-TREE", tree_file)

    _, tree_image, headers = request_multipart(
        "/treeImage",
        {},
        "file",
        "result.treefile",
        tree_file,
        timeout=120,
    )
    assert_nonempty("Tree rendering", tree_image)
    if not tree_image.startswith(b"\x89PNG\r\n\x1a\n"):
        raise RuntimeError("Tree rendering did not return a PNG file")

    print(json.dumps({"status": "all bioinformatics API smoke tests passed"}))


if __name__ == "__main__":
    main()
