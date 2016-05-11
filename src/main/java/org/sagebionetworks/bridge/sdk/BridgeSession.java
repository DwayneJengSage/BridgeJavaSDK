package org.sagebionetworks.bridge.sdk;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.Set;

import org.sagebionetworks.bridge.sdk.models.subpopulations.ConsentStatus;
import org.sagebionetworks.bridge.sdk.models.subpopulations.SubpopulationGuid;
import org.sagebionetworks.bridge.sdk.models.users.SharingScope;

import com.google.common.collect.ImmutableMap;

class BridgeSession implements Session {

    private static final String NOT_AUTHENTICATED = "This session has been signed out; create a new session to retrieve a valid client.";

    private SharingScope sharingScope;
    private String sessionToken;
    private Set<String> dataGroups;
    private String id;
    private Map<SubpopulationGuid,ConsentStatus> consentStatuses; 
    
    BridgeSession(UserSession session) {
        checkNotNull(session, "%s cannot be null", "UserSession");
        
        this.sessionToken = session.getSessionToken();
        this.sharingScope = session.getSharingScope();
        this.dataGroups = session.getDataGroups();
        this.id = session.getId();
        this.consentStatuses = session.getConsentStatuses();
    }
    
    /**
     * Check that the client is currently authenticated, throwing an exception 
     * if it is not.
     * @throws IllegalStateException
     */
    public void checkSignedIn() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
    }
    
    @Override
    public String getSessionToken() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
        return sessionToken;
    }
    
    @Override
    public boolean isConsented() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
        return ConsentStatus.isUserConsented(consentStatuses);
    }
    
    @Override
    public SharingScope getSharingScope() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
        return sharingScope;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public Set<String> getDataGroups() {
        return dataGroups;
    }
    
    @Override
    public Map<SubpopulationGuid,ConsentStatus> getConsentStatuses() {
        return consentStatuses;
    }
    
    @Override
    public UserClient getUserClient() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
        return new BridgeUserClient(this);
    }

    @Override
    public DeveloperClient getDeveloperClient() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
        return new BridgeDeveloperClient(this);
    }
    
    @Override
    public ResearcherClient getResearcherClient() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
        return new BridgeResearcherClient(this);
    }

    @Override
    public AdminClient getAdminClient() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
        return new BridgeAdminClient(this);
    }

    /** {@inheritDoc} */
    @Override
    public WorkerClient getWorkerClient() {
        checkState(isSignedIn(), NOT_AUTHENTICATED);
        return new BridgeWorkerClient(this);
    }

    @Override
    public boolean isSignedIn() {
        return sessionToken != null;
    }

    @Override
    public synchronized void signOut() {
        if (sessionToken != null) {
            new BaseApiCaller(this).post(ClientProvider.getConfig().getSignOutApi());
            sessionToken = null;
        }
    }
    
    void setUserSession(UserSession session) {
        this.sessionToken = session.getSessionToken();
        this.sharingScope = session.getSharingScope();
        this.dataGroups = session.getDataGroups();
        this.id = session.getId();
        this.consentStatuses = session.getConsentStatuses();
    }
    
    void setSharingScope(SharingScope sharingScope) {
        this.sharingScope = sharingScope;
    }
    
    void setConsentStatuses(Map<SubpopulationGuid,ConsentStatus> consentStatuses) {
        this.consentStatuses = (consentStatuses == null) ? ImmutableMap.<SubpopulationGuid,ConsentStatus>of() : 
            ImmutableMap.copyOf(consentStatuses);
    }    
    
    @Override
    public String toString() {
        return String.format("BridgeSession [sessionToken=%s, sharingScope=%s, dataGroups=%s, consentStatuses=%s]", 
                sessionToken, sharingScope.name().toLowerCase(), dataGroups, consentStatuses);
    }

}
