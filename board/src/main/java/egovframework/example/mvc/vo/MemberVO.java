package egovframework.example.mvc.vo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/** security UserDetails의 구현체인 회원 객체 */
public class MemberVO implements UserDetails {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private Long id;
	private String username;
	private String password;
	private String name;
	private Role role;
	//private ArrayList<GrantedAuthority> authorities; 권한이 여러 개일 경우 사용
	private boolean useYn;
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(role.name()));
        return auth;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return useYn;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return useYn;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return useYn;
	}

	@Override
	public boolean isEnabled() {
		return useYn;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUseYn() {
		return useYn;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
