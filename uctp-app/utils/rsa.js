
import jsencrypt from '@/components/jsencrypt/jsencrypt.vue'

let key =
  '-----BEGIN PUBLIC KEY-----' +
  'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAICnVHS7keeBQuyMoObYmnsAPKGaAgbJDWNRXB9b5ah3Wf41FaDFCgJvgrfzYopHKkQtrX1AKgLdWJw0SdYkML0CAwEAAQ' +
  '-----END PUBLIC KEY-----'



export function rsaEncrypt(msg) {
  var encryptMsg = jsencrypt.setEncrypt(key, msg)
  return encryptMsg
}