package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 25.01.2015
 */
public enum Skill {

    BERSERK("Берсерк", "Чем меньше у героя остаётся здоровья, тем больше урона врагу он наносит.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    COMRADE("Боевой товарищ", "Герой обращается со спутником как с боевым товарищем, благодаря чему улучшаются все боевые особенности спутника.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    STRONG("Боец", "Удары закалённых в постоянных сражениях бойцов наносят больше урона противникам.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    WANDERER("Бродяга", "Бродяги истоптали тысячи тропинок и всегда найдут кратчайшую дорогу, поэтому путешествуют быстрее остальных.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    ECONOMIC("Бухгалтер", "Герои с бухгалтерской жилкой ответственно подходят не только к своему имуществу, но и к имуществу спутника. Способность улучшают денежные особенности спутника.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    EXTRA_FAST("Быстрее ветра", "В столкновении со столь быстрым существом далеко не каждому удаётся устоять под градом стремительных атак.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    FAST("Быстрый", "Обладатель этой способности имеет хорошую реакцию и действует в бою быстрее.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    WITCHCRAFT("Ведовство", "Герой, сведущий в нетрадиционных областях знаний, иногда может восстановить здоровье необычного спутника.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    WARRIOR("Воин", "Воин большую часть времени тратит на физические тренировки, благодаря чему наносит больший физический урон, имеет хорошую защиту от физических атак, но слабо противостоит магии и сам с трудом ей пользуется. Увеличение физических способностей сильнее, чем ослабление магических.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    MAGIC_MUSHROOM("Волшебный гриб", "Герой всегда носит с собой особые грибы, съев один из которых в бою, на некоторое время существенно увеличивает наносимый урон.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, false),
    HEALING("Врачевание", "Умение обращаться с ниткой, иголкой и хирургическим ножом позволяет иногда восстановить немного здоровья живому спутнику.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    GARGOYLE("Горгулья", "Подобно горгулье, обладатель этой способности имеет увеличенную защиту от всех типов атак.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    EXTRA_STRONG("Громила", "Лучше не попадать под удары этого монстра — громила наносит намного больший урон чем другие противники.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    BUSINESSMAN("Делец", "Герой имеет больше шансов получить артефакт в награду за выполнение задания.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    DIPLOMATIC("Дипломатичный", "Некоторые герои приноровились выполнять задание особенно хитро и тщательно, тем самым увеличивая своё влияние на участников задания.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    THOUGHTFUL("Заботливый", "Окружённый заботой героя, спутник увеличивает своё максимальное здоровье.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    FREEZING("Заморозка", "Противника пронзает ужасный холод, замедляя его движения.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, false),
    THRIFTY("Запасливый", "Запасливый герой не любит расставаться с добычей, поэтому носит с собой рюкзак большего размера.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    THICK("Здоровяк", "Герои и монстры, которые много кушали в детстве, становятся немного здоровее остальных.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    IMPROVISER("Импровизатор", "Герой всегда готов помочь своему спутнику в любых его делах, что усиливает его необычные особенности.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    EXTRA_THIN("Кожа да кости", "Обладатель способности не может похвастаться хорошим запасом здоровья.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    SOCIABILITY("Коммуникабельность", "Хороший разговор сближает лучше кровавой стычки, коммуникабельный герой быстрее увеличивает слаженность живого спутника.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    CRITICAL_HIT("Критический удар", "Удача благосклонна к герою — урон от любого удара может существенно увеличиться.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    MAGE("Маг", "Маг всё своё усердие направляет в совершенствование магических умений, поэтому имеет увеличенный магический урон, защиту от магии и уменьшенные физический урон и защиту от физических атак. Увеличение магических способностей сильнее, чем ослабление физических.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    MAGE_MECHANICS("Магомеханика", "С помощью плоскогубцев, проволоки и толики магии магомеханик иногда может отремонтировать своего магомеханического спутника.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    SLOW("Медленный", "Не всем существам посчастливилось быть быстроногими, некоторых природа обделила и их скорость в бою обычно чуть меньше, чем у противников.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    EXTRA_SLOW("Неповоротливый", "У обладателя этой способности наверняка в роду были ленивцы — в бою он движется очень медленно.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    NINJA("Ниндзя", "Ниндзя может уклониться от атаки противника.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    SERVICE("Обслуживание", "Каждому магомеханическому спутнику требуется регулярная смазка, или подзарядка кристаллов, или ещё какая-нибудь заумная операция. Чем ответственнее герой относится к обслуживанию своего спутника, тем быстрее растёт его слаженность.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    GIFTED("Одарённый", "Одарённые герои быстрее получают опыт.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    LAST_CHANCE("Последний шанс", "Способность для тех, кто действительно сражается до конца. Иногда позволяет пережить смертельный удар и продолжить сражаться с 1 здоровьем (может спасать владельца несколько раз за бой).", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    PICKY("Придирчивый", "Герой с большей вероятностью получает редкие и эпические артефакты.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    RUN_UP_PUSH("Разбег-толчок", "Герой разбегается и наносит урон противнику. Враг будет оглушён и пропустит один или несколько ходов атаки.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, true),
    REGENERATION("Регенерация", "Во время боя герой может восстановить часть своего здоровья.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, false),
    SACREDNESS("Сакральность", "Некоторые спутники настолько необычны, что герою приходится учиться думать как его напарник. Если герою удаётся найти схожие струны в душе спутника, то их слаженность начинает расти быстрее.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    WEAK("Слабый", "Слабые монстры иногда стараются компенсировать небольшой недостаток урона за счёт хитрости, но мало у кого это получается.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    COHERENCE("Товарищ", "Путешествия со спутником сложны и требуют от героя особых навыков. Умение по-товарищески относиться к спутнику определяет максимальную слаженность спутника. Она увеличивается на 20 за уровень способности.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    EXTRA_THICK("Толстяк", "Монстр может похвастаться отменным здоровьем и очень большой живучестью.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    HUCKSTER("Торгаш", "Увеличивается цена продажи и уменьшается цена покупки предметов.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    EXTRA_WEAK("Тростинка", "Обычные атаки монстра наносят очень мало урона.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    STRONG_HIT("Тяжёлый удар", "Если хорошенько размахнуться и правильно ударить, то удар получится намного сильнее и болезненней.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, true),
    KILLER("Убийца", "Ориентируясь на короткий бой, убийца совершенствует свои атакующие способности в ущерб защитным.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.PASSIVE, false),
    HIT("Удар", "Каждый уважающий себя герой должен быть в состоянии ударить противника… или пнуть.", 1, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, true),
    VAMPIRE_STRIKE("Удар вампира", "Секретный удар, лечащий героя на величину, пропорциональную нанесённому урону.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, true),
    SPEEDUP("Ускорение", "Воин накачивает себя магической энергией, временно улучшая свои рефлексы.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, false),
    CHARISMA("Харизматичный", "Герой настолько обаятелен, что умудряется получать больше денег за выполнение заданий.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    WALKER("Ходок", "Ходоки знают как лучше использовать дорожные особенности спутников.", 5, SkillType.COMPANION, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    THIN("Худой", "Мир таков, что не все существа обладают крепкими мышцами и хорошим запасом здоровья. Кому-то приходится мириться с уменьшенным количеством HP.", 5, SkillType.COMBAT, SkillAvailability.MONSTERS, SkillMethod.PASSIVE, false),
    SIDESTEP("Шаг в сторону", "Герой быстро меняет свою позицию, дезориентируя противника, из-за чего тот начинает промахиваться.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, false),
    FIREBALL("Шар огня", "Герой запускает в противника шар огня, нанося большой урон и поджигая врага.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, true),
    DANDY("Щёголь", "Увеличивает вероятность траты денег на заточку, ремонт и покупку артефактов.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    ETHEREAL_MAGNET("Эфирный магнит", "Герой притягивает к себе магию и увеличивает вероятность критического срабатывания помощи хранителя.", 5, SkillType.PEACE, SkillAvailability.PLAYERS, SkillMethod.PASSIVE, false),
    POISON_CLOUD("Ядовитое облако", "Вокруг противника сгущается ядовитое облако, вызывающее сильное отравление, из-за которого враг начинает постепенно терять здоровье.", 5, SkillType.COMBAT, SkillAvailability.ALL, SkillMethod.ACTIVE, true),
    ;

    public final String name;
    public final String description;
    public final int maxLevel;
    public final SkillType type;
    public final SkillAvailability availability;
    public final SkillMethod method;
    public final boolean isDirectDamage;

    private Skill(final String name, final String description, final int maxLevel, final SkillType type,
                  final SkillAvailability availability, final SkillMethod method, final boolean isDirectDamage) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        this.type = type;
        this.availability = availability;
        this.method = method;
        this.isDirectDamage = isDirectDamage;
    }

    public String getName() {
        return name;
    }

}
