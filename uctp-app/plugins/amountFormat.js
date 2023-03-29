// 金额添加千分位
const comdify = function(n) {
  if (!n) return n
  n = (Math.round(parseFloat(n) * 100) / 100).toString()
  const str = n.split('.')
  if (str.length > 1) {
    if (str[1].length < 2 && str.length > 1) {
      str[1] = str[1] + '0'
    }
  }
  const re = /\d{1,3}(?=(\d{3})+$)/g
  const n1 = str[0].replace(re, '$&,')
  return str.length > 1 && str[1] ? `${n1}.${str[1]}` : `${n1}.00`
}
// 去除千分位中的‘，’
const delcommafy = function(num) {
  if (!num) return num
  num = num.toString()
  num = num.replace(/,/gi, '')
  return num
}

export default {
	// 转换为千分位格式
	getComdify(el) {
	  const inputVal = el ? el : el == 0 ? '0' : ''
	  return comdify(delcommafy(inputVal))
	},
	// 转换为字符串
	getDelcommafy(el) {
	  const inputVal = el || ''
	  return delcommafy(inputVal)
	}
}
