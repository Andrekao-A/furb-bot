package app.entity;

import java.io.*;
import javax.persistence.*;
import java.util.*;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFilter;
import cronapi.rest.security.CronappSecurity;


/**
 * Classe que representa a tabela CONVERSATION
 * @generated
 */
@Entity
@Table(name = "\"CONVERSATION\"")
@XmlRootElement
@CronappSecurity
//@JsonFilter("app.entity.Conversation")
public class Conversation implements Serializable {

  /**
   * UID da classe, necessário na serialização
   * @generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * @generated
   */
  @Id
  @Column(name = "id", nullable = false, insertable=true, updatable=true)
  private java.lang.String id = UUID.randomUUID().toString().toUpperCase();

  /**
  * @generated
  */
  @Column(name = "context", nullable = true, unique = false, insertable=true, updatable=true)
  
  private java.lang.String context;

  /**
   * Construtor
   * @generated
   */
  public Conversation(){
  }


  /**
   * Obtém id
   * return id
   * @generated
   */
  
  public java.lang.String getId(){
    return this.id;
  }

  /**
   * Define id
   * @param id id
   * @generated
   */
  public Conversation setId(java.lang.String id){
    this.id = id;
    return this;
  }

  /**
   * Obtém context
   * return context
   * @generated
   */
  
  public java.lang.String getContext(){
    return this.context;
  }

  /**
   * Define context
   * @param context context
   * @generated
   */
  public Conversation setContext(java.lang.String context){
    this.context = context;
    return this;
  }

  /**
   * @generated
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Conversation object = (Conversation)obj;
    if (id != null ? !id.equals(object.id) : object.id != null) return false;
    return true;
  }

  /**
   * @generated
   */
  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

}
