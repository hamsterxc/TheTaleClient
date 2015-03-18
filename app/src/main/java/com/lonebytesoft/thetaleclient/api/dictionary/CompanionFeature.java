package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.02.2015
 */
public enum CompanionFeature {

    AGGRESSIVE("агрессивный", "увеличивает агрессивность героя", CompanionFeatureType.PERSISTENT),
    PEP("бодрость духа", "даёт небольшой бонус к магическому урону героя", CompanionFeatureType.BATTLE),
    FIGHTER("боец", "немного увеличивает инициативу героя, в бою может применить способность «Удар»", CompanionFeatureType.BATTLE),
    LIGHTFOOTED("быстроногий", "постоянный большой бонус к скорости героя", CompanionFeatureType.ROAD),
    INSPIRATION("воодушевление", "воодушевляет героя на подвиги, снижая время бездействия между заданиями", CompanionFeatureType.ROAD),
    TEMPORARY("временный", "спутник с небольшой вероятностью может покинуть героя при посещении города", CompanionFeatureType.PERSISTENT),
    PACK("вьючный", "2 дополнительных места в рюкзаке", CompanionFeatureType.PERSISTENT),
    ROBBER("грабитель", "В каждом городе крадёт у горожан что-нибудь полезное, возможно, даже экипировку", CompanionFeatureType.MONEY),
    GOON("громила", "немного увеличивает инициативу героя, в бою может применить способность «Тяжёлый удар»", CompanionFeatureType.BATTLE),
    FREIGHT("грузовой", "4 дополнительных места в рюкзаке", CompanionFeatureType.PERSISTENT),
    CONSCIENTIOUS("добросовестный", "слаженность растёт быстрее обычного", CompanionFeatureType.PERSISTENT),
    EXPENSIVE("дорогой", "при потере спутника герой получает весьма дорогие запчасти, обращаемые в деньги.", CompanionFeatureType.PERSISTENT),
    EATER("едок", "при каждом посещении города герой тратит деньги на еду для спутника", CompanionFeatureType.MONEY),
    SLED("ездовой", "постоянный небольшой бонус к скорости героя", CompanionFeatureType.ROAD),
    SLED_FLYER("ездовой летун", "часто переносит героя на небольшое расстояние по воздуху", CompanionFeatureType.ROAD),
    SPARE_PARTS("запчасти", "забирает 4 места в рюкзаке для запчастей", CompanionFeatureType.PERSISTENT),
    FAMOUS("известный", "находит политически важную работу, задания героя оказывают большее влияние на мир", CompanionFeatureType.UNCOMMON),
    PROMPT("исполнительный", "слаженность растёт очень быстро", CompanionFeatureType.PERSISTENT),
    CAMOUFLAGE("камуфляж", "благодаря незаметности, реже получает урон в бою", CompanionFeatureType.BATTLE),
    PICKPOCKET("карманник", "В каждом городе крадёт из карманов горожан немного денег", CompanionFeatureType.MONEY),
    HEALER("лекарь", "ускоряет лечение героя на отдыхе", CompanionFeatureType.ROAD),
    LAZY("ленивый", "ленится вместе с героем и увеличивает время бездействия между заданиями", CompanionFeatureType.ROAD),
    FLYER("летающий", "перемещаясь не только вокруг но и над противником, значительно реже получает урон в бою", CompanionFeatureType.BATTLE),
    PARAPHERNALIA("личные вещи", "забирает 2 места в рюкзаке для своих вещей", CompanionFeatureType.PERSISTENT),
    DEXTEROUS("ловкий", "малый бонус к инициативе героя", CompanionFeatureType.BATTLE),
    SLOW("медлительный", "постоянный штраф к скорости героя", CompanionFeatureType.ROAD),
    CUTE("милый", "симпатичен горожанам, герой получает небольшой бонус к денежной награде за задания", CompanionFeatureType.MONEY),
    PEACEFUL("миролюбивый", "увеличивает миролюбие героя", CompanionFeatureType.PERSISTENT),
    SHIVERS("мороз по коже", "сильно пугает горожан, герой получает значительно меньше денег в награду за задание.", CompanionFeatureType.MONEY),
    FREEZER("морозко", "немного увеличивает инициативу героя, в бою может применить способность «Заморозка»", CompanionFeatureType.BATTLE),
    WISE("мудрый", "спутник иногда делится мудростью с героем, давая тому немного опыта.", CompanionFeatureType.UNCOMMON),
    PIGMEAT("не жилец", "при ранении может получить 2 дополнительные единицы урона", CompanionFeatureType.PERSISTENT),
    UNSOCIABLE("нелюдимый", "спутник может покинуть героя при посещении города", CompanionFeatureType.PERSISTENT),
    CLUMSY("неповоротливый", "малый штраф к инициативе героя", CompanionFeatureType.BATTLE),
    HULKY("неуклюжий", "большой штраф к инициативе героя", CompanionFeatureType.BATTLE),
    SPECIAL("особый", "особый спутник, которого нельзя получить обычным способом", CompanionFeatureType.PERSISTENT),
    HAWKEYE("острый глаз", "издали высматривает врагов, снижая вероятность встречи с ними", CompanionFeatureType.ROAD),
    POISONER("отравитель", "немного увеличивает инициативу героя, в бою может применить способность «Ядовитое облако»", CompanionFeatureType.BATTLE),
    HUNTER("охотник", "помогает герою сражаться аккуратнее, благодаря чему увеличивает шанс найти уцелевшую в бою добычу", CompanionFeatureType.BATTLE),
    CHARMING("очаровательный", "очень симпатичен горожанам, герой получает крупный бонус к денежной награде за задания", CompanionFeatureType.MONEY),
    VERY_RARE("очень редкий", "спутник встречается очень редко", CompanionFeatureType.PERSISTENT),
    ARSONIST("поджигатель", "немного увеличивает инициативу героя, в бою может применить способность «Огненный шар»", CompanionFeatureType.BATTLE),
    VILE("подлый", "уменьшает честь героя", CompanionFeatureType.PERSISTENT),
    DEVOURER("пожиратель", "после боя иногда ест труп врага, пополняя себе хиты. Не ест конструктов, нежить, демонов и стихийных существ.", CompanionFeatureType.UNCOMMON),
    WORSHIPPER("поклонник", "возносит хвалу Хранителю вместе с героем и с небольшой вероятностью даёт бонусную энергию", CompanionFeatureType.UNCOMMON),
    ENERGETIC("прилив сил", "даёт небольшой бонус к физическому урону героя", CompanionFeatureType.BATTLE),
    GLUTTONOUS("прожорливый", "при каждом посещении города герой тратит много денег на еду для спутника", CompanionFeatureType.MONEY),
    DRUNKARD("пьяница", "спутник пропивает случайный предмет из рюкзака при посещении героем города", CompanionFeatureType.MONEY),
    REGENERATION("регенерация", "во время отдыха может восстановить своё здоровье", CompanionFeatureType.UNCOMMON),
    RARE("редкий", "спутник встречается реже обычного", CompanionFeatureType.PERSISTENT),
    INDEPENDENT("самостоятельный", "может кормиться сам, снижает стоимость кормёжки (способностей «едок» и «прожорливый»)", CompanionFeatureType.MONEY),
    PRAYER("сан", "возносит хвалу Хранителю вместе с героем и с хорошей вероятностью даёт бонусную энергию", CompanionFeatureType.UNCOMMON),
    SPIRIT_CONTACT("связной", "служит маяком для Хранителя и увеличивает шанс критической помощи", CompanionFeatureType.UNCOMMON),
    DISCREET("сдержанный", "склоняет героя к балансу между агрессивностью и миролюбием", CompanionFeatureType.PERSISTENT),
    CANNY("себе на уме", "склоняет героя к балансу между честью и бесчестием", CompanionFeatureType.PERSISTENT),
    TROTTER("скакун", "постоянный бонус к скорости героя", CompanionFeatureType.ROAD),
    COMPLEX("сложный", "ухаживая за спутником, герой может получить немного опыта", CompanionFeatureType.UNCOMMON),
    DEADLY_FEARFUL("смертельно страшный", "распугивает всех, кого встречает, вероятность встретить врага стремится к нулю", CompanionFeatureType.PERSISTENT),
    SEDATE("степенный", "постоянный небольшой штраф к скорости героя", CompanionFeatureType.ROAD),
    FEARFUL("страшный", "пугает горожан, герой получает меньше денег за задание", CompanionFeatureType.MONEY),
    IMPETUOUS("стремительный", "большой бонус к инициативе героя", CompanionFeatureType.BATTLE),
    OBSTINATE("строптивый", "слаженность растёт очень медленно", CompanionFeatureType.PERSISTENT),
    RAM("таран", "немного увеличивает инициативу героя, в бою может применить способность «Разбег-толчок»", CompanionFeatureType.BATTLE),
    TELEPORTER("телепортатор", "периодически переносит героя между городами или ключевыми точками задания", CompanionFeatureType.ROAD),
    BODYGUARD("телохранитель", "чаще защищает героя в бою", CompanionFeatureType.BATTLE),
    GNAWER("терзатель", "растерзывает врагов в бою так сильно, что уменьшается шанс найти уцелевшую в бою добычу", CompanionFeatureType.BATTLE),
    TRADER("торгаш", "помогает герою торговаться, увеличивая цены продажи и уменьшая цены покупки", CompanionFeatureType.MONEY),
    COWARD("трусливый", "реже защищает героя в бою", CompanionFeatureType.BATTLE),
    PUNY("тщедушный", "при ранении может получить дополнительную единицу урона", CompanionFeatureType.PERSISTENT),
    DRAUGHT("тягловой", "6 дополнительных мест в рюкзаке", CompanionFeatureType.PERSISTENT),
    STUBBORN("упрямый", "слаженность растёт медленнее обычного", CompanionFeatureType.PERSISTENT),
    CAD("хам", "хамит горожанам, герою не доверяют политически важную работу, поэтому он оказывает меньшее влияние на мир", CompanionFeatureType.UNCOMMON),
    HONEST("честный", "увеличивает честь героя", CompanionFeatureType.PERSISTENT),
    NOISY("шумный", "так сильно шумит, что привлекает внимание большего количества врагов", CompanionFeatureType.ROAD),
    EXORCIST("экзорцист", "спутник может изгнать встречного демона", CompanionFeatureType.BATTLE),
    ;

    private final String name;
    private final String description;
    private final CompanionFeatureType type;

    CompanionFeature(final String name, final String description, final CompanionFeatureType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CompanionFeatureType getType() {
        return type;
    }

}
