package com.alfanshter.jatimpark.Session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context?) {
val privateMode = 0
val privateName ="login"
var Pref :SharedPreferences?=context?.getSharedPreferences(privateName,privateMode)
    var editor : SharedPreferences.Editor?=Pref?.edit()

private val islogin = "login"
fun setLogin(check: Boolean){
    editor?.putBoolean(islogin,check)
    editor?.commit()
}

    fun getLogin():Boolean?
    {
        return Pref?.getBoolean(islogin,false)
    }

    private val iduser = "iduser"
    fun setIduser(check:String)
    {
        editor?.putString(iduser,check)
        editor?.commit()
    }

    fun getIduser():String?
    {
        return Pref?.getString(iduser,"")
    }

    private val longitude = "longitude"
    fun setlongitude(check:String)
    {
        editor?.putString(longitude,check)
        editor?.commit()
    }

    fun getlongitude():String?
    {
        return Pref?.getString(longitude,"")
    }

    private val latidude = "latidude"
    fun setLatidude(check:String)
    {
        editor?.putString(latidude,check)
        editor?.commit()
    }

    fun getLatidude():String?
    {
        return Pref?.getString(latidude,"")
    }

    private val kunci = "kunci"
    fun setKunci (check: String)
    {
        editor?.putString(kunci,check)
        editor?.commit()
    }

    fun getKunci():String?
    {
        return Pref?.getString(kunci,"")
    }

//status nama
    private val isnama = "nama"
    fun setNama(check: Boolean){
        editor?.putBoolean(isnama,check)
        editor?.commit()
    }

    fun getNama():Boolean?
    {
        return Pref?.getBoolean(isnama,false)
    }

    //nama
    private val profil = "profil"
    fun setprofil(check:String)
    {
        editor?.putString(profil,check)
        editor?.commit()
    }

    fun getprofil():String?
    {
        return Pref?.getString(profil,"")
    }





    private val id_status_user = "id_status_user"
    fun setIDStatusUser(id_status_users:String?){
        editor?.putString(id_status_user, id_status_users)
        editor?.commit()
    }
    fun getIDStatusUser():String?{
        return Pref?.getString(id_status_user,"")
    }






}