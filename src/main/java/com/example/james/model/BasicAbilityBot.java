package com.example.james.model;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.objects.ReplyFlow;
import org.telegram.abilitybots.api.toggle.CustomToggle;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BasicAbilityBot extends AbilityBot {
	
	private static final String BOT_TOKEN = "1741357913:AAHpDPeJBeXkFuy4OGgCngE3m5ovxGhuPA0";
	private static final String BOT_USERNAME = "FryoBot";
	private long updateCount = 0;
	private static final CustomToggle toggle = new CustomToggle()
			.turnOff("ban").toggle("promote", "upgrade");
	
	
	public BasicAbilityBot() {
		super(BOT_TOKEN, BOT_USERNAME, toggle);
	}

	private long getUpdateCount() {
		return updateCount;
	}
	
	@Override
	public long creatorId() {
		return 1758360687;
	}
	

	@Override
	protected boolean checkGlobalFlags(Update update) {
		return true;
	}

	private Predicate<Update> isReplyToMessage(String msg) {
		return upd -> {
			var reply = upd.getMessage().getReplyToMessage();
			return reply.hasText() && reply.getText().equalsIgnoreCase(msg);
		};
	}
	
	private Predicate<Update> isReplyToBot() {
		return upd -> upd.getMessage().getReplyToMessage().getFrom()
				.getUserName().equalsIgnoreCase(getBotUsername());
	}
	
	public Ability commentSentPhoto() {
		var file = new File("https://images.pexels.com/photos/1044056/"
				+ "pexels-photo-1044056.jpeg?cs=srgb&dl=pexels-jack-geoghegan-1044056.jpg&fm=jpg");
		var inputFile = new InputFile(file);
		var photo = new SendPhoto();
		photo.setCaption("test photo caption");
		
		return Ability.builder()
				.name(DEFAULT)
				.flag(Flag.PHOTO)
				.locality(Locality.ALL)
				.privacy(Privacy.PUBLIC)
				.input(0)
				.action(ctx -> silent.send("Daang, what a nice phot!", ctx.chatId()))
				.post(ctx -> silent.send("Look at mine XD", ctx.chatId()))
				.action(ctx -> {
					photo.setChatId(ctx.chatId().toString());
					photo.setPhoto(inputFile);
					try {
						sender.sendPhoto(photo);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
				})
				.build();
	}
	
	public Ability sayHelloWord() {
		return Ability
				.builder()
				.name("hello")
				.info("says Cudowna blondynka z Torunia studiująca rachunkowość...")
				.locality(Locality.ALL)
				.privacy(Privacy.PUBLIC)
				.action(ctx -> silent.send("Cudowna blondynka z Torunia "
						+ "studiująca rachunkowość... To musi być Natalia!", ctx.chatId()))
				.build();
	}
	
	public Ability sayHi() {
		return Ability
				.builder()
				.name("hi")
				.info("say hi to a friend")
				.locality(Locality.USER)
				.privacy(Privacy.PUBLIC)
				.input(1)
				.action(ctx -> silent.send("Hi " + ctx.firstArg(), ctx.chatId()))
				.build();
	}
	
	public Ability showUpdateCount( ) {
		return Ability
				.builder()
				.name("updates")
				.info("# of updates sent to " + BOT_USERNAME)
				.locality(Locality.USER)
				.privacy(Privacy.CREATOR)
				.input(1)
				.action(ctx -> {
					if (Long.valueOf(ctx.firstArg()) <= getUpdateCount())
						silent.send("No worries", ctx.chatId());
					else
						silent.send("Time to wake up", ctx.chatId());
				})
				.build();
		
	}
	
	public Ability greet() {
		return Ability
				.builder()
				.name("yo")
				.info("introduce yourself")
				.locality(Locality.USER)
				.privacy(Privacy.PUBLIC)
				.action(ctx -> {
					var msg = "Yo " + ctx.user().getFirstName() + "!"
							+ "\nI'll order any pizza you want. Just tell me, you know.";
					silent.send(msg, ctx.chatId());
				})
				.build();
	}
	
	public Ability scared() {
		return Ability
				.builder()
				.name("scared")
				.info("they're scared of admins!")
				.locality(Locality.ALL)
				.privacy(Privacy.ADMIN)
				.action(ctx -> silent.send("Your Highness!", ctx.chatId()))
				.post(ctx -> silent.send("It's not going to happen again ...", ctx.chatId()))
				.build();
	}
	
	public Ability playWithMe() {
		var msg = "Play with me!";
		return Ability
				.builder()
				.name("play")
				.info("Do you want to play with me?")
				.locality(Locality.ALL)
				.privacy(Privacy.PUBLIC)
				.action(ctx -> silent.forceReply(msg, ctx.chatId()))
				.reply((bot, upd) -> silent.send("It's been nice to play with you", upd.getMessage().getChatId()),
						Flag.MESSAGE, Flag.REPLY, isReplyToBot(), isReplyToMessage(msg))
				.build();	
	}
	
	public ReplyFlow directCommand() {
		Reply saidLeft = Reply.of((bot, upd) -> silent.send("Sir, I've gone left", upd.getMessage()
				.getChatId()),
				hasMessageWith("left"));
		Reply saidRight = Reply.of((bot, upd) -> silent.send("Sir, I've gone right", upd.getMessage()
				.getChatId()),
				hasMessageWith("right"));
		
		return ReplyFlow.builder(db)
				.action((bot, upd) -> silent.send("Command me to go left or right!", upd.getMessage().getChatId()))
				.onlyIf(hasMessageWith("wake up"))
				.next(saidLeft)
				.next(saidRight)
				.build();
	}

	private Predicate<Update> hasMessageWith(String msg) {
		return upd -> upd.getMessage().getText().equalsIgnoreCase(msg);
	}

	@Override
	public void onUpdateReceived(Update update) {
		super.onUpdateReceived(update);
		updateCount++;
	}
	
	

}
