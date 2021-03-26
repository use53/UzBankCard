package uz.fintech.uzbankcard.utils

import uz.fintech.uzbankcard.model.CardModel

interface IPreferences{

    var isSingupName:String
    var isSingupPassword:String
    var isCodenSave:Boolean
    var isCardSaveBoolean:Boolean
    var isCardModelSave:String
    var isCallNumber:String
}
