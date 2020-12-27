package cn.shineiot.basekotlin

import androidx.lifecycle.liveData
import cn.shineiot.base.mvvm.BaseViewModel
import cn.shineiot.base.mvvm.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel :BaseViewModel() {

    fun getData() =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            try {
                //http
                emit(Resource.success(null))
            }catch (e : Exception){
                emit(Resource.error(e,null))
            }
        }

}