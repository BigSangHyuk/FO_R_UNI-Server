package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.exception.post.DeadlineNotFoundException;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ICalService {

    private final PostRepository postRepository;

    public byte[] getEventInfo(Long postId) throws IOException {
        Calendar event = createEvent(postId);
        return toICalendar(event);
    }

    private Calendar createEvent(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        LocalDate deadline = post.getDeadline();

        if (deadline == null) {
            throw new DeadlineNotFoundException();
        }

        Calendar calendar = new Calendar();
        VEvent event = new VEvent();
        VAlarm alarm = new VAlarm();

        addProperties(event, post);
        addAlarm(alarm, event, deadline);
        addProperties(calendar, event);

        return calendar;
    }

    private byte[] toICalendar(Calendar event) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(event, baos);
        return baos.toByteArray();
    }

    private void addProperties(Calendar calendar, VEvent event) {
        calendar.getProperties().add(new ProdId("-//BigSangHyuk//FourUni//KR"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getComponents().add(event);
    }

    private void addProperties(VEvent event, Post post) {
        event.getProperties().add(new Summary(post.getTitle()));
        event.getProperties().add(new DtStart(new Date(java.sql.Date.valueOf(post.getDeadline().plusDays(1)))));
        event.getProperties().add(new DtEnd(new Date(java.sql.Date.valueOf(post.getDeadline().plusDays(2)))));
        event.getProperties().add(new Location("인천대학교"));
        event.getProperties().add(new Url(URI.create(post.getNoticeUrl())));
        event.getProperties().add(new Uid(UUID.randomUUID().toString()));
    }

    private void addAlarm(VAlarm alarm, VEvent event, LocalDate deadline) {
        LocalDate dayBefore = deadline.minusDays(1);
        java.util.Date alarmDate = Date.from(dayBefore.atTime(18, 0).toInstant(ZoneOffset.ofHours(9)));
        alarm.getProperties().add(new Trigger(new DateTime(alarmDate)));
        event.getAlarms().add(alarm);
    }
}
