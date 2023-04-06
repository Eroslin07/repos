
import jsencrypt from '@/components/jsencrypt/jsencrypt.vue'

let key =
  `MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwRKEgraXxlxt8NMrVwaM
ILtczXQLAmThLE7OZxyKpBSD2aTNcJ8JFtC0PVS/UR4ALtlB0yOM3llWxscW25qm
jqm9g16o3+GpHH8JgnMbJB6+2qlvojSeDQYhr5mjdMmo8DS2EaYZVR7DZFPD7oLc
YQmQRJGlA7bqzAS9mNoECYYQ/FJ31u79h0AsDT35vpiOaKc523WVO6cPAvSmtr77
/Zbu+9deirgI9s+ebibh1UyHD12xnJMTKqqbMenI87CIt1YwyMt8sNiizDU5JdpL
1Q9OmdU9/x0Qm1QBefYt32cz4i3Z7ZCjt4N7VWKVbUL9KAn7Fm66icJ287DRv61M
KQIDAQAB`



export function rsaEncrypt(msg) {
  var encryptMsg = jsencrypt.setEncrypt(key, msg)
  return encryptMsg
}