package com.example.james.model;

import org.glassfish.jersey.message.internal.OutboundMessageContext;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BasicAbilityBot extends AbilityBot {
	
	private static final String BOT_TOKEN = "1741357913:AAHpDPeJBeXkFuy4OGgCngE3m5ovxGhuPA0";
	private static final String BOT_USERNAME = "FryoBot";
	private long updateCount = 0;
	
	public BasicAbilityBot() {
		super(BOT_TOKEN, BOT_USERNAME);
	}

	private long getUpdateCount() {
		return updateCount;
	}
	
	@Override
	public long creatorId() {
		return 1758360687;
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

	@Override
	public void onUpdateReceived(Update update) {
		super.onUpdateReceived(update);
		updateCount++;
	}
	
	

}
