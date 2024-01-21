/*
package com.ru.mai.dpp.applicationmicroservice.service;

import com.ru.mai.dpp.applicationmicroservice.dto.CreateInvitationDTO;
import com.ru.mai.dpp.applicationmicroservice.dto.CreateRequestDTO;
import com.ru.mai.dpp.applicationmicroservice.entity.*;
import com.ru.mai.dpp.applicationmicroservice.enums.ApplicationStatus;
import com.ru.mai.dpp.applicationmicroservice.enums.ApplicationType;
import com.ru.mai.dpp.applicationmicroservice.enums.UserRole;
import com.ru.mai.dpp.applicationmicroservice.exception.AccessDeniedException;
import com.ru.mai.dpp.applicationmicroservice.mappers.ApplicationDTOMapper;
import com.ru.mai.dpp.applicationmicroservice.mappers.UserDTOMapper;
import com.ru.mai.dpp.applicationmicroservice.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.testng.Assert;

import java.util.Optional;

*/
/**
 * @author a_krevetochka
 * @version 17.08.2023
 *//*


@ExtendWith(MockitoExtension.class)
public class JoinToCommandServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private RoleInProjectRepository roleInProjectRepository;
    @Mock
    private ApplicationFilterRepository applicationFilterRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDTOMapper userDTOMapper;
    @Mock
    private UserTeamsRepository userTeamsRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private ApplicationDTOMapper applicationDTOMapper;
    @Mock
    private StudentProfileRepository studentProfileRepository;

    private JoinToCommandService joinToCommandService;

    @BeforeEach
    void init(){
        joinToCommandService = new JoinToCommandService (studentProfileRepository,applicationFilterRepository, notificationRepository, applicationRepository, roleInProjectRepository, userRepository, userTeamsRepository, mapper, applicationDTOMapper);
    }

    @Test
    void createInvitation() {
        //mock

        User initiator = new User();
        initiator.setRole(UserRole.STUDENT);

        User recipient = new User();
        recipient.setFio("vasya");

        Team team = new Team();
        team.setTitle("hello world");

        RoleInProject TeamLeadRole = RoleInProject.builder().id(1).title("teamLead").build();

        UserTeams ut = UserTeams.builder().role(TeamLeadRole).team(team).build();

        RoleInProject role = new RoleInProject();
        role.setId(2);
        role.setTitle("title");

        CreateInvitationDTO dto = new CreateInvitationDTO(0, 0);

        ArgumentCaptor<Notification> argNotification = ArgumentCaptor.forClass(Notification.class);
        ArgumentCaptor<Application> argApplication = ArgumentCaptor.forClass(Application.class);

        //returns

        when(roomsRepository.findAll(session)).thenReturn(expectedRooms);
        Mockito.doReturn(Optional.of(recipient)).when(userRepository).findById(dto.getRecipientId());

        Mockito.doReturn(Optional.of(ut)).when(userTeamsRepository).findByMember_Id(initiator.getId());
        Mockito.doReturn(Optional.of(role)).when(roleInProjectRepository).findById(dto.getRoleId());

        //call
        boolean isApplicationCreated = joinToCommandService.createInvitation(initiator, dto);

        //asserts
        Assert.assertTrue(isApplicationCreated);

        Mockito.verify(applicationRepository).save(argApplication.capture());
        Assert.assertEquals(initiator, argApplication.getValue().getInitiator());
        Assert.assertEquals(recipient, argApplication.getValue().getRecipient());
        Assert.assertEquals(team, argApplication.getValue().getTeam());
        Assert.assertEquals(ApplicationStatus.IN_REVIWEVING, argApplication.getValue().getStatus());
        Assert.assertEquals(ApplicationType.INVITE_TO_THE_TEAM, argApplication.getValue().getType());
        Mockito.verify(notificationRepository, Mockito.times(2)).save(argNotification.capture());
        Assert.assertEquals(recipient, argNotification.getAllValues().get(0).getRecipient());
        Assert.assertEquals(initiator, argNotification.getAllValues().get(1).getRecipient());

    }


    @Test
    void createInvitationIsNotTeamLead() {
        User initiator = new User();
        Optional<User> recipient = Optional.of(new User());
        Team team = new Team();
        RoleInProject role = RoleInProject.builder().id(2).build();
        Optional<UserTeams> ut = Optional.of(UserTeams.builder().role(role).team(team).build());
        initiator.setRole(UserRole.STUDENT);
        CreateInvitationDTO dto = new CreateInvitationDTO(0, 0);

        Mockito.doReturn(recipient).when(userRepository).findById(0);
        Mockito.doReturn(ut).when(userTeamsRepository).findByMember_Id(initiator.getId());

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            joinToCommandService.createInvitation(initiator, dto);
        });
    }

    @Test
    void createInvitationTeamWaitingForApprove() {
        User initiator = new User();
        Optional<User> recipient = Optional.of(new User());
        Team team = new Team();
        team.setAdminApplication(Application.builder().status(ApplicationStatus.IN_REVIWEVING).build());
        RoleInProject role = RoleInProject.builder().id(1).build();
        Optional<UserTeams> ut = Optional.of(UserTeams.builder().role(role).team(team).build());
        initiator.setRole(UserRole.STUDENT);
        CreateInvitationDTO dto = new CreateInvitationDTO(0, 0);

        Mockito.doReturn(recipient).when(userRepository).findById(0);
        Mockito.doReturn(ut).when(userTeamsRepository).findByMember_Id(initiator.getId());

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            joinToCommandService.createInvitation(initiator, dto);
        });
    }

    @Test
    void createInvitationExistAnotherInvitation() {
        User initiator = new User();
        Optional<User> recipient = Optional.of(new User());
        Team team = new Team();
        team.setId(1);
        RoleInProject role = RoleInProject.builder().id(1).build();
        Optional<UserTeams> ut = Optional.of(UserTeams.builder().role(role).team(team).build());
        initiator.setRole(UserRole.STUDENT);
        CreateInvitationDTO dto = new CreateInvitationDTO(0, 0);
        Application otherInvitation = new Application();
        otherInvitation.setStatus(ApplicationStatus.IN_REVIWEVING);

        Mockito.doReturn(recipient).when(userRepository).findById(dto.getRecipientId());
        Mockito.doReturn(ut).when(userTeamsRepository).findByMember_Id(initiator.getId());
        Mockito.doReturn(Optional.of(otherInvitation)).when(applicationRepository).findDublicateApplication(initiator.getId(), team.getId(), dto.getRecipientId(), ApplicationType.INVITE_TO_THE_TEAM, ApplicationStatus.IN_REVIWEVING);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            joinToCommandService.createInvitation(initiator, dto);
        });
    }

    @Test
    void createRequest() {
        //mock

        User initiator = new User();
        initiator.setRole(UserRole.STUDENT);

        CreateRequestDTO dto = new CreateRequestDTO(1);

        User recipient = User.builder().role(UserRole.STUDENT).build();
        recipient.setId(dto.getRecipientId());

        Team team = new Team();

        ArgumentCaptor<Notification> argNotification = ArgumentCaptor.forClass(Notification.class);
        ArgumentCaptor<Application> argApplication = ArgumentCaptor.forClass(Application.class);

        Mockito.doReturn(Optional.of(recipient)).when(userRepository).findById(dto.getRecipientId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(recipient).team(team).role(RoleInProject.builder().id(1).build()).build())).when(userTeamsRepository).findByMember_Id(recipient.getId());
        Mockito.doReturn(Optional.of(StudentProfile.builder().dppGroup(DPPGroup.builder().program(new Program()).build()).build())).when(studentProfileRepository).findByOwner_Id(initiator.getId());

        boolean isCreated = joinToCommandService.createRequest(initiator, dto);

        Assert.assertTrue(isCreated);
        Mockito.verify(applicationRepository).save(argApplication.capture());
        Assert.assertEquals(initiator, argApplication.getValue().getInitiator());
        Assert.assertEquals(recipient, argApplication.getValue().getRecipient());
        Assert.assertEquals(team, argApplication.getValue().getTeam());
        Assert.assertEquals(ApplicationStatus.IN_REVIWEVING, argApplication.getValue().getStatus());
        Assert.assertEquals(ApplicationType.REQUEST_TO_THE_TEAM, argApplication.getValue().getType());
        Mockito.verify(notificationRepository, Mockito.times(2)).save(argNotification.capture());
        Assert.assertEquals(recipient, argNotification.getAllValues().get(0).getRecipient());
        Assert.assertEquals(initiator, argNotification.getAllValues().get(1).getRecipient());


    }

    @Test
    void createRequestIsNotStudent() {

        User initiator = new User();
        initiator.setRole(UserRole.ADMIN);

        CreateRequestDTO dto = Mockito.mock(CreateRequestDTO.class);

        Assert.assertThrows(AccessDeniedException.class, () -> {
            joinToCommandService.createRequest(initiator, dto);
        });
    }

    @Test
    void createRequestToYourOwnTeam() {

        User initiator = new User();
        initiator.setRole(UserRole.STUDENT);
        initiator.setId(1);

        CreateRequestDTO dto = new CreateRequestDTO(3);

        User recipient = User.builder().role(UserRole.STUDENT).build();
        recipient.setId(dto.getRecipientId());

        Team team = new Team();

        Mockito.doReturn(Optional.of(recipient)).when(userRepository).findById(dto.getRecipientId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(recipient).team(team).role(RoleInProject.builder().id(1).build()).build())).when(userTeamsRepository).findByMember_Id(recipient.getId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(initiator).team(team).role(RoleInProject.builder().id(2).build()).build())).when(userTeamsRepository).findByMember_Id(initiator.getId());

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            joinToCommandService.createRequest(initiator, dto);
        });


    }

    @Test
    void createRequestInitiatorTeamWaitingForApprove() {

        User initiator = new User();
        initiator.setRole(UserRole.STUDENT);
        initiator.setId(1);

        CreateRequestDTO dto = new CreateRequestDTO(3);

        User recipient = User.builder().role(UserRole.STUDENT).build();
        recipient.setId(dto.getRecipientId());

        Team team = new Team();
        team.setAdminApplication(Application.builder().status(ApplicationStatus.ACCEPTED).build());

        Mockito.doReturn(Optional.of(recipient)).when(userRepository).findById(dto.getRecipientId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(recipient).team(new Team()).role(RoleInProject.builder().id(1).build()).build())).when(userTeamsRepository).findByMember_Id(recipient.getId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(initiator).team(team).role(RoleInProject.builder().id(2).build()).build())).when(userTeamsRepository).findByMember_Id(initiator.getId());

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            joinToCommandService.createRequest(initiator, dto);
        });
    }

    @Test
    void createRequestRecipientTeamWaitingForApprove() {

        User initiator = new User();
        initiator.setRole(UserRole.STUDENT);
        initiator.setId(1);

        CreateRequestDTO dto = new CreateRequestDTO(3);

        User recipient = User.builder().role(UserRole.STUDENT).build();
        recipient.setId(dto.getRecipientId());

        Team team = new Team();
        team.setAdminApplication(Application.builder().status(ApplicationStatus.ACCEPTED).build());

        Mockito.doReturn(Optional.of(recipient)).when(userRepository).findById(dto.getRecipientId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(recipient).team(team).role(RoleInProject.builder().id(1).build()).build())).when(userTeamsRepository).findByMember_Id(recipient.getId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(initiator).team(new Team()).role(RoleInProject.builder().id(2).build()).build())).when(userTeamsRepository).findByMember_Id(initiator.getId());

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            joinToCommandService.createRequest(initiator, dto);
        });

    }

    @Test
    void createRequestExistsSameApplication() {

        User initiator = new User();
        initiator.setRole(UserRole.STUDENT);
        initiator.setId(1);

        CreateRequestDTO dto = new CreateRequestDTO(3);

        User recipient = User.builder().role(UserRole.STUDENT).build();
        recipient.setId(dto.getRecipientId());

        Team team = new Team();
        team.setId(1);

        Mockito.doReturn(Optional.of(StudentProfile.builder().dppGroup(DPPGroup.builder().program(new Program()).build()).build())).when(studentProfileRepository).findByOwner_Id(initiator.getId());
        Mockito.doReturn(Optional.of(recipient)).when(userRepository).findById(dto.getRecipientId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(recipient).team(team).role(RoleInProject.builder().id(1).build()).build())).when(userTeamsRepository).findByMember_Id(recipient.getId());
        Mockito.doReturn(Optional.of(UserTeams.builder().member(initiator).team(new Team()).role(RoleInProject.builder().id(2).build()).build())).when(userTeamsRepository).findByMember_Id(initiator.getId());
        Mockito.doReturn(Optional.of(Application.class)).when(applicationRepository).findDublicateApplication(initiator.getId(), 1, recipient.getId(), ApplicationType.REQUEST_TO_THE_TEAM, ApplicationStatus.IN_REVIWEVING);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            joinToCommandService.createRequest(initiator, dto);
        });
    }

    @Test
    void setInvitationAccepted() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        Integer applicationId = 1;

        User initiator = new User();
        initiator.setFio("vasya");

        RoleInProject role = RoleInProject.builder().id(1).title("role").build();

        Application application = new Application(1, null, null, null, initiator, responser, null, new Team(), role, ApplicationStatus.IN_REVIWEVING, ApplicationType.INVITE_TO_THE_TEAM, null, null, null);

        Mockito.doReturn(Optional.of(application)).when(applicationRepository).findById(applicationId);

        boolean isAccepted = joinToCommandService.setAccepted(responser, applicationId);

        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);
        ArgumentCaptor<UserTeams> userTeamsArgumentCaptor = ArgumentCaptor.forClass(UserTeams.class);

        Assert.assertTrue(isAccepted);
        Mockito.verify(applicationRepository).save(applicationArgumentCaptor.capture());
        Assert.assertEquals(ApplicationStatus.ACCEPTED, applicationArgumentCaptor.getValue().getStatus());
        Assert.assertEquals(responser, applicationArgumentCaptor.getValue().getResponser());
        Mockito.verify(applicationRepository, Mockito.times(1)).removeRequestsByUser(application.getRecipient(), ApplicationStatus.ACCEPTED, ApplicationType.REQUEST_TO_THE_TEAM);
        Mockito.verify(applicationRepository, Mockito.times(1)).removeInvitationsByUser(application.getRecipient(), ApplicationStatus.ACCEPTED, ApplicationType.INVITE_TO_THE_TEAM);
        Mockito.verify(notificationRepository, Mockito.times(2)).save(notificationArgumentCaptor.capture());
        Assert.assertEquals(initiator, notificationArgumentCaptor.getAllValues().get(0).getRecipient());
        Assert.assertEquals(responser, notificationArgumentCaptor.getAllValues().get(1).getRecipient());
//        Assert.assertEquals("Пользователь "+"присоединился к Вашей команде", notificationArgumentCaptor.getAllValues().get(0).getContent());
        Mockito.verify(userTeamsRepository).save(userTeamsArgumentCaptor.capture());
        Assert.assertEquals(responser, userTeamsArgumentCaptor.getValue().getMember());
        Assert.assertEquals(userTeamsArgumentCaptor.getValue().getRole(), role);

    }

    @Test
    void setRequestAcceptedInitiatorInAnotherTeam() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);
        responser.setFio("я получил, я ответил");

        Integer applicationId = 1;

        User initiator = new User();
        initiator.setId(100);
        initiator.setFio("я инициировал");

        User prevTeamlead = User.builder().fio("а я костя предыдущий тимлид").build();
        Team kostyaTeam = Team.builder().teamlead(prevTeamlead).build();
        UserTeams kostyaInTeam = UserTeams.builder().team(kostyaTeam).member(prevTeamlead).build();

        Application application = new Application(1, null, null, null, initiator, responser, null, new Team(), null, ApplicationStatus.IN_REVIWEVING, ApplicationType.REQUEST_TO_THE_TEAM, null, null, null);

        Mockito.doReturn(Optional.of(application)).when(applicationRepository).findById(applicationId);
        Mockito.doReturn(Optional.of(UserTeams.builder().role(RoleInProject.builder().id(1).build()).build())).when(userTeamsRepository).findByMember_Id(responser.getId());
        Mockito.doReturn(Optional.of(kostyaInTeam)).when(userTeamsRepository).findByMember_Id(initiator.getId());

        boolean isAccepted = joinToCommandService.setAccepted(responser, applicationId);

        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);
        ArgumentCaptor<UserTeams> userTeamsArgumentCaptor = ArgumentCaptor.forClass(UserTeams.class);

        Assert.assertTrue(isAccepted);
        Mockito.verify(applicationRepository).save(applicationArgumentCaptor.capture());
        Assert.assertEquals(ApplicationStatus.ACCEPTED, applicationArgumentCaptor.getValue().getStatus());
        Assert.assertEquals(responser, applicationArgumentCaptor.getValue().getResponser());
        Mockito.verify(applicationRepository, Mockito.times(1)).removeRequestsByUser(initiator, ApplicationStatus.ACCEPTED, ApplicationType.REQUEST_TO_THE_TEAM);
        Mockito.verify(applicationRepository, Mockito.times(1)).removeInvitationsByUser(initiator, ApplicationStatus.ACCEPTED, ApplicationType.INVITE_TO_THE_TEAM);
        Mockito.verify(notificationRepository, Mockito.times(3)).save(notificationArgumentCaptor.capture());

        Assert.assertEquals(initiator, notificationArgumentCaptor.getAllValues().get(0).getRecipient());

        Assert.assertEquals(prevTeamlead, notificationArgumentCaptor.getAllValues().get(1).getRecipient());

        Assert.assertEquals(responser, notificationArgumentCaptor.getAllValues().get(2).getRecipient());

        Mockito.verify(userTeamsRepository).save(userTeamsArgumentCaptor.capture());
        Assert.assertEquals(initiator, userTeamsArgumentCaptor.getValue().getMember());

        Assert.assertEquals(notificationArgumentCaptor.getAllValues().get(1).getContent(), "Пользователь покинул Вашу команду");

        Mockito.verify(userTeamsRepository, Mockito.times(1)).removeByMember_Id(initiator.getId());
    }

    @Test
    void setAcceptedChangedBefore() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        Integer applicationId = 1;

        Mockito.doReturn(Optional.of(Application.builder().status(ApplicationStatus.ACCEPTED).build())).when(applicationRepository).findById(applicationId);

        Assert.assertThrows(AccessDeniedException.class, () -> {
            joinToCommandService.setAccepted(responser, applicationId);
        });
    }


    @Test
    void setAcceptedRecipientIsNotTeamLead() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        Application application = new Application();
        application.setStatus(ApplicationStatus.IN_REVIWEVING);
        application.setId(1);
        application.setType(ApplicationType.REQUEST_TO_THE_TEAM);

        Integer applicationId = 1;

        Mockito.doReturn(Optional.of(application)).when(applicationRepository).findById(applicationId);
        Mockito.doReturn(Optional.of(UserTeams.builder().role(RoleInProject.builder().id(2).build()).build())).when(userTeamsRepository).findByMember_Id(responser.getId());

        Assert.assertThrows(AccessDeniedException.class, () -> {
            joinToCommandService.setAccepted(responser, applicationId);
        });
    }

    @Test
    void setAcceptedResponserIsNotRecipient() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        Application application = new Application();
        application.setStatus(ApplicationStatus.IN_REVIWEVING);
        application.setId(1);
        application.setType(ApplicationType.INVITE_TO_THE_TEAM);
        application.setRecipient(new User());

        Integer applicationId = 1;

        Mockito.doReturn(Optional.of(application)).when(applicationRepository).findById(applicationId);

        Assert.assertThrows(AccessDeniedException.class, () -> {
            joinToCommandService.setAccepted(responser, applicationId);
        });
    }

    @Test
    void setRejected() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        User initiator = new User();
        initiator.setFio("name");

        Application application = new Application();
        application.setStatus(ApplicationStatus.IN_REVIWEVING);
        application.setId(1);
        application.setType(ApplicationType.INVITE_TO_THE_TEAM);
        application.setRecipient(responser);
        application.setInitiator(initiator);

        Integer applicationId = 1;

        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);

        Mockito.doReturn(Optional.of(application)).when(applicationRepository).findById(applicationId);

        boolean isRejected = joinToCommandService.setRejected(responser, applicationId);

        Assert.assertTrue(isRejected);

        Mockito.verify(applicationRepository).save(applicationArgumentCaptor.capture());
        Assert.assertEquals(responser, applicationArgumentCaptor.getValue().getResponser());
        Assert.assertEquals(applicationArgumentCaptor.getValue().getStatus(), ApplicationStatus.REJECTED);

        Mockito.verify(notificationRepository, Mockito.times(2)).save(notificationArgumentCaptor.capture());
        Assert.assertEquals(notificationArgumentCaptor.getAllValues().get(0).getRecipient(), initiator);
        Assert.assertEquals(notificationArgumentCaptor.getAllValues().get(0).getContent(), "заявка отклонена");

        Assert.assertEquals(responser, notificationArgumentCaptor.getAllValues().get(1).getRecipient());

    }

    @Test
    void setRejectedApplicationDoesNotExist() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        var applicationId = 1;

        Mockito.doReturn(Optional.empty()).when(applicationRepository).findById(applicationId);

        Assert.assertThrows(EntityNotFoundException.class, () -> {
            joinToCommandService.setRejected(responser, applicationId);
        });
    }

    @Test
    void setRejectedChangedBefore() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        var applicationId = 1;
        Application application = new Application();
        application.setStatus(ApplicationStatus.ACCEPTED);

        Mockito.doReturn(Optional.of(application)).when(applicationRepository).findById(applicationId);

        Assert.assertThrows(AccessDeniedException.class, () -> {
            joinToCommandService.setRejected(responser, applicationId);
        });
    }

    @Test
    void setRejectedRequestResponserIsNotTeamLead() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        var applicationId = 1;
        Application application = new Application();
        application.setStatus(ApplicationStatus.IN_REVIWEVING);
        application.setType(ApplicationType.REQUEST_TO_THE_TEAM);
        application.setId(applicationId);

        UserTeams ut = UserTeams.builder().role(RoleInProject.builder().id(2).build()).build();

        Mockito.doReturn(Optional.of(application)).when(applicationRepository).findById(applicationId);
        Mockito.doReturn(Optional.of(ut)).when(userTeamsRepository).findByMember_Id(responser.getId());

        Assert.assertThrows(AccessDeniedException.class, () -> {
            joinToCommandService.setRejected(responser, applicationId);
        });
    }

    @Test
    void setRejectedResponserIsNotRecipient() {

        User responser = new User();
        responser.setRole(UserRole.STUDENT);
        responser.setId(1);

        Application application = new Application();
        application.setStatus(ApplicationStatus.IN_REVIWEVING);
        application.setId(1);
        application.setType(ApplicationType.INVITE_TO_THE_TEAM);
        application.setRecipient(new User());

        Integer applicationId = 1;

        Mockito.doReturn(Optional.of(application)).when(applicationRepository).findById(applicationId);

        Assert.assertThrows(AccessDeniedException.class, () -> {
            joinToCommandService.setRejected(responser, applicationId);
        });
    }


    @Test
    void getApplicationsWithAlTypesUserDoesNotExist() {
        var username = "username";
        Mockito.doReturn(Optional.empty()).when(userRepository).findByUsername(username);
        Assert.assertThrows(EntityNotFoundException.class, () -> {
            joinToCommandService.getApplicationsWithAllTypes(username);
        });
    }
}
*/
