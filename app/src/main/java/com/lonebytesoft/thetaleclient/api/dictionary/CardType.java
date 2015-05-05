package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum CardType {

    SUDDEN_FINDING(48, CardRarity.COMMON, "внезапная находка", "Герой получает случайный бесполезный предмет или артефакт.", CardTargetType.NONE),
    COINS_HANDFUL(10, CardRarity.COMMON, "горсть монет", "Герой получает 1000 монет.", CardTargetType.NONE),
    DEATH_HAND(52, CardRarity.COMMON, "длань Смерти", "Мгновенно убивает монстра, с которым сражается герой.", CardTargetType.NONE),
    COMPANION_CARE(105, CardRarity.COMMON, "забота о ближнем", "Текущей целью трат героя становится лечение спутника.", CardTargetType.NONE),
    EQUIPMENT_CARE(44, CardRarity.COMMON, "забота об имуществе", "Текущей целью трат героя становится починка артефакта.", CardTargetType.NONE),
    ENERGY_DROP(5, CardRarity.COMMON, "капля энергии", "Вы получаете 10 единиц дополнительной энергии.", CardTargetType.NONE),
    SHOP_IMPULSE(41, CardRarity.COMMON, "магазинный импульс", "Текущей целью трат героя становится покупка артефакта.", CardTargetType.NONE),
    UNCHANGEABLE_COIN(53, CardRarity.COMMON, "неразменная монета", "Создаёт в указанном городе 20 «даров Хранителей». Город будет постепенно переводить их в продукцию, пока дары не кончатся.", CardTargetType.PLACE),
    NEW_CIRCUMSTANCES(78, CardRarity.COMMON, "новые обстоятельства", "Увеличивает влияние, которое окажет герой после выполнения текущего задания, на 500 единиц.", CardTargetType.NONE),
    COMMON_COMPANION(90, CardRarity.COMMON, "обычный спутник", "Герой получает спутника, указанного в названии карты. Если у героя уже есть спутник, он покинет героя.", CardTargetType.NONE),
    RESPITE(100, CardRarity.COMMON, "передышка", "Восстанавливает спутнику 1 здоровья.", CardTargetType.NONE),
    STRANGE_ITCH(40, CardRarity.COMMON, "странный зуд", "Текущей целью трат героя становится лечение.", CardTargetType.NONE),
    EXCELLENCE_AIM(42, CardRarity.COMMON, "стремление к совершенству", "Текущей целью трат героя становится заточка артефакта.", CardTargetType.NONE),
    KNOWLEDGE_AIM(43, CardRarity.COMMON, "тяга к знаниям", "Текущей целью трат героя становится обучение.", CardTargetType.NONE),
    LUCKY_THOUGHT(74, CardRarity.COMMON, "удачная мысль", "Увеличивает опыт, который герой получит за выполнение текущего задания, на 25 единиц.", CardTargetType.NONE),
    FOUR_DIRECTIONS(99, CardRarity.COMMON, "четыре стороны", "Спутник героя навсегда покидает его.", CardTargetType.NONE),
    ALTERNATIVE(39, CardRarity.UNCOMMON, "альтернатива", "Изменяет список предлагаемых герою способностей (при выборе новой способности).", CardTargetType.NONE),
    AMNESIA(84, CardRarity.UNCOMMON, "амнезия", "Преобразует опыт героя на текущем уровне в дополнительную энергию по курсу 6 опыта за 1 энергии.", CardTargetType.NONE),
    EQUIPMENT_STYLING(34, CardRarity.UNCOMMON, "вкусы в экипировке", "Сбрасывает задержку на изменение предпочтения «экипировка».", CardTargetType.NONE),
    MAGIC_GRINDSTONE(88, CardRarity.UNCOMMON, "волшебное точило", "Улучшает случайный артефакт из экипировки героя.", CardTargetType.NONE),
    MAGIC_POT(54, CardRarity.UNCOMMON, "волшебный горшочек", "Создаёт в указанном городе 80 «даров Хранителей». Город будет постепенно переводить их в продукцию, пока дары не кончатся.", CardTargetType.PLACE),
    TEMPER(16, CardRarity.UNCOMMON, "вспыльчивость", "Уменьшает миролюбие героя на 25 единиц.", CardTargetType.NONE),
    OTHER_CONCERNS(47, CardRarity.UNCOMMON, "другие заботы", "Отменяет текущее задание героя.", CardTargetType.NONE),
    ENEMY_KNOWLEDGE(29, CardRarity.UNCOMMON, "знание врага", "Сбрасывает задержку на изменение предпочтения «любимая добыча».", CardTargetType.NONE),
    BORED_THING(36, CardRarity.UNCOMMON, "наскучившая вещь", "Сбрасывает задержку на изменение предпочтения «любимая вещь».", CardTargetType.NONE),
    UNCOMMON_COMPANION(91, CardRarity.UNCOMMON, "необычный спутник", "Герой получает спутника, указанного в названии карты. Если у героя уже есть спутник, он покинет героя.", CardTargetType.NONE),
    NEW_HOMELAND(30, CardRarity.UNCOMMON, "новая родина", "Сбрасывает задержку на изменение предпочтения «родной город».", CardTargetType.NONE),
    NEW_LOOK(95, CardRarity.UNCOMMON, "новый взгляд", "Сбрасывает задержку на изменение предпочтения «отношение со спутником».", CardTargetType.NONE),
    NEW_ENEMY(32, CardRarity.UNCOMMON, "новый противник", "Сбрасывает задержку на изменение предпочтения «противник».", CardTargetType.NONE),
    NEW_WAY(98, CardRarity.UNCOMMON, "новый путь", "Сбрасывает все способности героя.", CardTargetType.NONE),
    NEW_FRIEND(31, CardRarity.UNCOMMON, "новый соратник", "Сбрасывает задержку на изменение предпочтения «соратник».", CardTargetType.NONE),
    METTLE_DETERMINATION(35, CardRarity.UNCOMMON, "определение лихости", "Сбрасывает задержку на изменение предпочтения «уровень риска».", CardTargetType.NONE),
    ARCHIVES_ERROR(70, CardRarity.UNCOMMON, "ошибка в архивах", "В документах города появляется дополнительная запись о помощи, полученной от героя.", CardTargetType.PLACE),
    FIGHTSTYLE_REVISION(37, CardRarity.UNCOMMON, "пересмотр стиля боя", "Сбрасывает задержку на изменение предпочтения «архетип».", CardTargetType.NONE),
    GOOD_DAYS(62, CardRarity.UNCOMMON, "погожие деньки", "Увеличивает бонус к начисляемому городу положительному влиянию на 0.25%.", CardTargetType.PLACE),
    PLANTAIN(101, CardRarity.UNCOMMON, "подорожник", "Восстанавливает спутнику 4 здоровья.", CardTargetType.NONE),
    USEFUL_GIFT(49, CardRarity.UNCOMMON, "полезный подарок", "Герой получает случайный артефакт.", CardTargetType.NONE),
    EPIPHANY(33, CardRarity.UNCOMMON, "прозрение", "Сбрасывает задержку на изменение предпочтения «религиозность».", CardTargetType.NONE),
    SPECIAL_OPERATION(79, CardRarity.UNCOMMON, "специальная операция", "Увеличивает влияние, которое окажет герой после выполнения текущего задания, на 2000 единиц.", CardTargetType.NONE),
    TRANQUILLITY(15, CardRarity.UNCOMMON, "спокойствие", "Увеличивает миролюбие героя на 25 единиц.", CardTargetType.NONE),
    TELEPORT(82, CardRarity.UNCOMMON, "телепорт", "Телепортирует героя до ближайшего города либо до ближайшей ключевой точки задания. Работает только во время движения по дорогам.", CardTargetType.NONE),
    COINS_PURSE(11, CardRarity.UNCOMMON, "увесистый кошель", "Герой получает 4000 монет.", CardTargetType.NONE),
    LUCKY_DAY(58, CardRarity.UNCOMMON, "удачный день", "Увеличивает бонус к начисляемому положительному влиянию советника на 0.25%.", CardTargetType.PERSON),
    AWFUL_WEATHER(66, CardRarity.UNCOMMON, "ужасная погода", "Увеличивает бонус к начисляемому городу негативному влиянию на 0.25%.", CardTargetType.PLACE),
    MODERATION(13, CardRarity.UNCOMMON, "умеренность", "Увеличивает честь героя на 25 единиц.", CardTargetType.NONE),
    FEY_MASTER(45, CardRarity.UNCOMMON, "фея-мастерица", "Чинит случайный артефакт из экипировки героя.", CardTargetType.NONE),
    ENERGY_POT(6, CardRarity.UNCOMMON, "чаша Силы", "Вы получаете 40 единиц дополнительной энергии.", CardTargetType.NONE),
    CLEAR_MIND(75, CardRarity.UNCOMMON, "чистый разум", "Увеличивает опыт, который герой получит за выполнение текущего задания, на 100 единиц.", CardTargetType.NONE),
    GLUTTONY(14, CardRarity.UNCOMMON, "чревоугодие", "Уменьшает честь героя на 25 единиц.", CardTargetType.NONE),
    SENSITIVITY(96, CardRarity.UNCOMMON, "чуткость", "Сбрасывает задержку на изменение предпочтения «эмпатия».", CardTargetType.NONE),
    TARDIS(83, CardRarity.RARE, "ТАРДИС", "Телепортирует героя в конечную точку назначения либо до ближайшей ключевой точки задания. Работает только во время движения по дорогам.", CardTargetType.NONE),
    GREED(20, CardRarity.RARE, "алчность", "Уменьшает миролюбие героя на 100 единиц.", CardTargetType.NONE),
    WHOREDOM(18, CardRarity.RARE, "блуд", "Уменьшает честь героя на 100 единиц.", CardTargetType.NONE),
    FIDELITY(17, CardRarity.RARE, "верность", "Увеличивает честь героя на 100 единиц.", CardTargetType.NONE),
    ENERGY_DONOR(85, CardRarity.RARE, "донор Силы", "Преобразует опыт героя на текущем уровне в дополнительную энергию по курсу 5 опыта за 1 энергии.", CardTargetType.NONE),
    FRIENDLINESS(19, CardRarity.RARE, "дружелюбие", "Увеличивает миролюбие героя на 100 единиц.", CardTargetType.NONE),
    DESOLATION(67, CardRarity.RARE, "запустение", "Увеличивает бонус к начисляемому городу негативному влиянию на 1.00%.", CardTargetType.PLACE),
    ENERGY_VORTEX(7, CardRarity.RARE, "магический вихрь", "Вы получаете 160 единиц дополнительной энергии.", CardTargetType.NONE),
    SUDDEN_PROFIT(59, CardRarity.RARE, "нежданная выгода", "Увеличивает бонус к начисляемому положительному влиянию советника на 1.00%.", CardTargetType.PERSON),
    SUDDEN_COMPLICATIONS(76, CardRarity.RARE, "неожиданные осложнения", "Увеличивает опыт, который герой получит за выполнение текущего задания, на 400 единиц.", CardTargetType.NONE),
    VALUES_REVISION(38, CardRarity.RARE, "пересмотр ценностей", "Сбрасывает задержку на изменение всех предпочтений.", CardTargetType.NONE),
    RARE_COMPANION(92, CardRarity.RARE, "редкий спутник", "Герой получает спутника, указанного в названии карты. Если у героя уже есть спутник, он покинет героя.", CardTargetType.NONE),
    RARE_ACQUISITION(50, CardRarity.RARE, "редкое приобретение", "Герой получает случайный редкий или эпический артефакт.", CardTargetType.NONE),
    HOLY_HONEY(102, CardRarity.RARE, "священный мёд", "Восстанавливает спутнику 16 здоровья.", CardTargetType.NONE),
    MAGIC_TABLECLOTH(55, CardRarity.RARE, "скатерть самобранка", "Создаёт в указанном городе 320 «даров Хранителей». Город будет постепенно переводить их в продукцию, пока дары не кончатся.", CardTargetType.PLACE),
    DABNGLAN_WORD(80, CardRarity.RARE, "слово Дабнглана", "Увеличивает влияние, которое окажет герой после выполнения текущего задания, на 8000 единиц.", CardTargetType.NONE),
    COINS_CHEST(12, CardRarity.RARE, "сундучок на счастье", "Герой получает 16000 монет.", CardTargetType.NONE),
    TRADING_DAY(63, CardRarity.RARE, "торговый день", "Увеличивает бонус к начисляемому городу положительному влиянию на 1.00%.", CardTargetType.PLACE),
    FALSE_RECOMMENDATIONS(71, CardRarity.RARE, "фальшивые рекомендации", "В документах города появляются дополнительные записи о помощи, полученной от героя в количестве 4 шт.", CardTargetType.PLACE),
    GREAT_GOD_BLESSING(46, CardRarity.EPIC, "благословение Великого Творца", "Чинит все артефакты из экипировки героя.", CardTargetType.NONE),
    DABNGLAN_BLESSING(81, CardRarity.EPIC, "благословление Дабнглана", "Увеличивает влияние, которое окажет герой после выполнения текущего задания, на 32000 единиц.", CardTargetType.NONE),
    DEBT_RECOVERY(86, CardRarity.EPIC, "взыскание долга", "Преобразует опыт героя на текущем уровне в дополнительную энергию по курсу 4 опыта за 1 энергии.", CardTargetType.NONE),
    MAGIC_INSTRUMENT(57, CardRarity.EPIC, "волшебный инструмент", "Полностью ремонтирует указанное строение.", CardTargetType.BUILDING),
    ANGER(24, CardRarity.EPIC, "гнев", "Уменьшает миролюбие героя на 400 единиц.", CardTargetType.NONE),
    CITY_HOLIDAY(64, CardRarity.EPIC, "городской праздник", "Увеличивает бонус к начисляемому городу положительному влиянию на 4.00%.", CardTargetType.PLACE),
    ANGEL_GIFT(51, CardRarity.EPIC, "дар Хранителя", "Герой получает случайный эпический артефакт.", CardTargetType.NONE),
    COUNCIL_FEAST(72, CardRarity.EPIC, "застолье в Совете", "В документах города появляются дополнительные записи о помощи, полученной от героя в количестве 16 шт.", CardTargetType.PLACE),
    REJUVENATING_APPLE(103, CardRarity.EPIC, "молодильное яблоко", "Восстанавливает спутнику 64 здоровья.", CardTargetType.NONE),
    RATS_INVASION(68, CardRarity.EPIC, "нашествие крыс", "Увеличивает бонус к начисляемому городу негативному влиянию на 4.00%.", CardTargetType.PLACE),
    GREAT_RICHES(56, CardRarity.EPIC, "несметные богатства", "Создаёт в указанном городе 1280 «даров Хранителей». Город будет постепенно переводить их в продукцию, пока дары не кончатся.", CardTargetType.PLACE),
    RESTRAINT(23, CardRarity.EPIC, "сдержанность", "Увеличивает миролюбие героя на 400 единиц.", CardTargetType.NONE),
    MODESTY(21, CardRarity.EPIC, "скромность", "Увеличивает честь героя на 400 единиц.", CardTargetType.NONE),
    HIDDEN_POTENTIAL(106, CardRarity.EPIC, "скрытый потенциал", "Улучшает на один уровень качество случайного экипированного не эпического артефакта.", CardTargetType.NONE),
    GZANZAR_WORD(77, CardRarity.EPIC, "слово Гзанзара", "Увеличивает опыт, который герой получит за выполнение текущего задания, на 1600 единиц.", CardTargetType.NONE),
    GIST_OF_THINGS(89, CardRarity.EPIC, "суть вещей", "Улучшает все артефакты из экипировки героя.", CardTargetType.NONE),
    VANITY(22, CardRarity.EPIC, "тщеславие", "Уменьшает честь героя на 400 единиц.", CardTargetType.NONE),
    LUCKY_SCAM(60, CardRarity.EPIC, "удачная афёра", "Увеличивает бонус к начисляемому положительному влиянию советника на 4.00%.", CardTargetType.PERSON),
    ENERGY_STORM(8, CardRarity.EPIC, "энергетический шторм", "Вы получаете 640 единиц дополнительной энергии.", CardTargetType.NONE),
    EPIC_COMPANION(93, CardRarity.EPIC, "эпический спутник", "Герой получает спутника, указанного в названии карты. Если у героя уже есть спутник, он покинет героя.", CardTargetType.NONE),
    GZANZAR_BLESSING(97, CardRarity.LEGENDARY, "благословление Гзанзара", "Увеличивает опыт, который герой получит за выполнение текущего задания, на 6400 единиц.", CardTargetType.NONE),
    PRIDE(26, CardRarity.LEGENDARY, "гордыня", "Уменьшает честь героя на 1600 единиц.", CardTargetType.NONE),
    AQUA_VITAE(104, CardRarity.LEGENDARY, "живая вода", "Восстанавливает спутнику 256 здоровья.", CardTargetType.NONE),
    INTRIGUES(73, CardRarity.LEGENDARY, "интриги", "В документах города появляются дополнительные записи о помощи, полученной от героя в количестве 64 шт.", CardTargetType.PLACE),
    LEGENDARY_COMPANION(94, CardRarity.LEGENDARY, "легендарный спутник", "Герой получает спутника, указанного в названии карты. Если у героя уже есть спутник, он покинет героя.", CardTargetType.NONE),
    PEACEFULNESS(27, CardRarity.LEGENDARY, "миролюбие", "Увеличивает миролюбие героя на 1600 единиц.", CardTargetType.NONE),
    INSIGHT(1, CardRarity.LEGENDARY, "озарение", "Герой получает новый уровень. Накопленный опыт не сбрасывается.", CardTargetType.NONE),
    CENTURY_CRIME(61, CardRarity.LEGENDARY, "преступление века", "Увеличивает бонус к начисляемому положительному влиянию советника на 16.00%.", CardTargetType.PERSON),
    ENERGY_RITUAL(87, CardRarity.LEGENDARY, "ритуал Силы", "Преобразует опыт героя на текущем уровне в дополнительную энергию по курсу 3 опыта за 1 энергии.", CardTargetType.NONE),
    CORNUCOPIA(0, CardRarity.LEGENDARY, "рог изобилия", "Создаёт в указанном городе 5120 «даров Хранителей». Город будет постепенно переводить их в продукцию, пока дары не кончатся.", CardTargetType.PLACE),
    HUMILITY(25, CardRarity.LEGENDARY, "смирение", "Увеличивает честь героя на 1600 единиц.", CardTargetType.NONE),
    ENERGY_FLURRY(9, CardRarity.LEGENDARY, "шквал Силы", "Вы получаете 2560 единиц дополнительной энергии.", CardTargetType.NONE),
    ECONOMIC_GROWTH(65, CardRarity.LEGENDARY, "экономический рост", "Увеличивает бонус к начисляемому городу положительному влиянию на 16.00%.", CardTargetType.PLACE),
    ECONOMIC_FALL(69, CardRarity.LEGENDARY, "экономический спад", "Увеличивает бонус к начисляемому городу негативному влиянию на 16.00%.", CardTargetType.PLACE),
    FURY(28, CardRarity.LEGENDARY, "ярость", "Уменьшает миролюбие героя на 1600 единиц.", CardTargetType.NONE),
    ;

    private final int code;
    private final CardRarity rarity;
    private final String name;
    private final String description;
    private final CardTargetType targetType;

    CardType(final int code, final CardRarity rarity, final String name,
             final String description, final CardTargetType targetType) {
        this.code = code;
        this.rarity = rarity;
        this.targetType = targetType;
        this.name = name;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public CardTargetType getTargetType() {
        return targetType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
