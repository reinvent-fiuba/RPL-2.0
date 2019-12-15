import binascii
import sys

with open(sys.argv[1]), 'rb') as f:
     content = f.read()
     print(binascii.hexlify(content))
