/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package org.tiogasolutions.jobs.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tiogasolutions.dev.common.DateUtils;
import org.tiogasolutions.push.jackson.CpObjectMapper;
import org.tiogasolutions.push.kernel.accounts.Account;
import org.tiogasolutions.push.kernel.accounts.AccountStore;
import org.tiogasolutions.push.kernel.accounts.DomainStore;
import org.tiogasolutions.push.kernel.accounts.actions.CreateAccountAction;
import org.tiogasolutions.push.kernel.actions.CreateDomainAction;
import org.tiogasolutions.push.kernel.clients.DomainProfileEntity;
import org.tiogasolutions.push.kernel.config.CouchServersConfig;
import org.tiogasolutions.push.kernel.execution.ExecutionManager;
import org.tiogasolutions.push.kernel.requests.PushRequest;
import org.tiogasolutions.push.kernel.requests.PushRequestStore;
import org.tiogasolutions.push.kernel.system.PluginManager;
import org.tiogasolutions.push.kernel.system.PushCouchServer;
import org.tiogasolutions.push.kernel.system.Session;
import org.tiogasolutions.push.pub.SmtpEmailPush;
import org.tiogasolutions.push.pub.common.Push;
import org.tiogasolutions.push.pub.common.UserAgent;

import java.time.ZoneId;
import java.util.*;

@Component
public class TestFixture {

  public static final ZoneId westCoastTimeZone = DateUtils.PDT;

  @Autowired
  private DomainStore domainStore;

  @Autowired
  private AccountStore accountStore;

  @Autowired
  private PushRequestStore pushRequestStore;

  public TestFixture() {
  }

  public UserAgent createUserAgent() {
    return new UserAgent(
        "agent-type", "agent-name", "agent-version", "agent-language", "agent-lang-tag",
        "os-type", "os-name", "os=produceer", "osproducer-url", "os-version-name", "os-version-number",
        "linux-distro"
    );
  }

  public Account createAccount() {
    CreateAccountAction createAccount = new CreateAccountAction(
        TestFixture.westCoastTimeZone,
        "test@jacobparr.com",
        "Unit", "Test",
        "testing123", "testing123"
    );

    Account account = new Account(createAccount);

    accountStore.create(account);
    return account;
  }

  public DomainProfileEntity createDomain(Account account) {
    CreateDomainAction createClient = new CreateDomainAction(account, "some-key", "some-password");
    DomainProfileEntity domain = account.add(createClient);

    domainStore.create(domain);
    return domain;
  }

  public List<PushRequest> createPushRequests_Emails(DomainProfileEntity domain) throws Exception {
    List<PushRequest> requests = new ArrayList<>();

    Push push = SmtpEmailPush.newPush("to@example.com", "from@example.com", "This is the subject", "<html><body><h1>Hello World</h1></body></html>", null);
    PushRequest pushRequest = new PushRequest(Push.CURRENT_API_VERSION, domain, push);
    requests.add(pushRequest);

    pushRequestStore.create(pushRequest);
    return requests;
  }

  public List<PushRequest> createPushRequests(DomainProfileEntity domain) throws Exception {
    Set<PushRequest> requests = new TreeSet<>();

    requests.addAll(createPushRequests_Emails(domain));

    return new ArrayList<>(requests);
  }

  public Session createSession() {
    return new Session(0, "test@example.com");
  }
}
