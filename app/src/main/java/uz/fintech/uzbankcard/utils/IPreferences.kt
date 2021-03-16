package uz.fintech.uzbankcard.utils

import uz.fintech.uzbankcard.model.CardModel

interface IPreferences{

    var isSingupName:String
    var isSingupPassword:String
    var isCallNumber:Boolean
    var isCardSaveBoolean:Boolean
    var isCardModelSave:String
}
