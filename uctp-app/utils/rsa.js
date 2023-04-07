
import jsencrypt from '@/components/jsencrypt/jsencrypt.vue'

let key ='MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWh3Nyt+5QqUXw1qHXM4k7lq98f9wA4iQgKK1LB1tr4uIgL/dls0LkBgY4oS/Dn3J0qHkpUTkTT84uMHey7cwdd9k90/65cpdawX0J0KO3S3Zwl9d5AJt7/hdSap3AcHw3dvlrZvvDJ72AaR3YUPujNM3dhLC7tsdDb3CxoJSBDQIDAQAB'



export function rsaEncrypt(msg) {
  var encryptMsg = jsencrypt.setEncrypt(key, msg)
  return encryptMsg
}