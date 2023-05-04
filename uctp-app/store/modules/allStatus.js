export default {
	state: {
		statusValue:{},
	},
	mutations: {
		setStatus:(state, value)=>{
			state.statusValue = value;
		}
	}
}
